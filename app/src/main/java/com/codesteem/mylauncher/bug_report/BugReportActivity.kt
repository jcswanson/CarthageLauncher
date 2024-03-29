package com.codesteem.mylauncher.bug_report

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.abedelazizshe.lightcompressorlibrary.CompressionListener
import com.abedelazizshe.lightcompressorlibrary.VideoCompressor
import com.abedelazizshe.lightcompressorlibrary.VideoQuality
import com.abedelazizshe.lightcompressorlibrary.config.Configuration
import com.abedelazizshe.lightcompressorlibrary.config.SaveLocation
import com.abedelazizshe.lightcompressorlibrary.config.SharedStorageConfiguration
import com.bumptech.glide.Glide
import com.codesteem.mylauncher.LoadingDialog
import com.codesteem.mylauncher.R
import com.codesteem.mylauncher.Resource
import com.codesteem.mylauncher.databinding.ActivityBugReportBinding
import com.codesteem.mylauncher.databinding.NotificationLayoutBinding
import com.codesteem.mylauncher.models.AppInfo
import com.codesteem.mylauncher.util.MySharedPreferences
import com.codesteem.mylauncher.util.NotificationListener
import com.tapadoo.alerter.Alerter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File


class BugReportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBugReportBinding
    private val viewModel: BugReportViewModel by viewModels()
    private var bugMedia: MutableList<Uri>? = null
    private var bugPhotos: MutableList<Uri>? = null
    private var bugVideos: MutableList<Uri>? = null
    private lateinit var compressedVideos: MutableList<Uri>
    private var loadingDialog: Dialog? = null
    private var loadingPercent = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBugReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intentFilter = IntentFilter("ACTION_NOTIFICATION_RECEIVED")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(notificationReceiver, intentFilter, RECEIVER_EXPORTED)
        } else {
            registerReceiver(notificationReceiver, intentFilter)
        }

        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_SCREEN_ON)
            addAction(Intent.ACTION_SCREEN_OFF)
        }
        registerReceiver(screenStateChangedReceiver, filter)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            enableFullScreenMode()
        }
        hideSystemUI()
        compressedVideos = ArrayList()
        loadingDialog = LoadingDialog(this, "Loading $loadingPercent")
        listenToUploading()

        binding.back.setOnClickListener {
            finish()
        }
        binding.attachBtn.setOnClickListener {
            val mediaType = ActivityResultContracts.PickVisualMedia.ImageAndVideo
            val request = PickVisualMediaRequest.Builder()
                .setMediaType(mediaType)
                .build()
            pickMultipleMediaItems.launch(request)
        }

        binding.sendBtn.setOnClickListener {
            if (bugMedia?.isEmpty() == true) {
                submit()
            } else {
                showConfirmDialog()
            }
        }
    }

    private val screenStateChangedReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when(intent?.action) {
                Intent.ACTION_SCREEN_OFF -> {
                    finish()
                }
            }
        }
    }
    private val notificationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "ACTION_NOTIFICATION_RECEIVED") {
                val title = intent.getStringExtra("title")
                val text = intent.getStringExtra("text")
                val pkg = intent.getStringExtra("pkg")
                val notificationTime = intent.getLongExtra("notificationTime",0L)
                val headsUp = intent.getBooleanExtra("heads-up",false)
                val receivedAppInfo: AppInfo? = intent.getParcelableExtra("app_info")
                val smallIconByteArray = intent.getByteArrayExtra("smallIcon")
                val isReply = intent.getBooleanExtra("replyAction", false)
                val isActivityInForeground = this@BugReportActivity.lifecycle.currentState.isAtLeast(
                    Lifecycle.State.RESUMED
                )
                val actions = intent.getParcelableArrayExtra("actions", Notification.Action::class.java)
                if (canShowNotification() && isActivityInForeground) {
                    showNotificationAlert(this@BugReportActivity, title, text, smallIconByteArray, isReply, actions!!)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(notificationReceiver)
        unregisterReceiver(screenStateChangedReceiver)
    }

    private fun canShowNotification(): Boolean {
        val now = System.currentTimeMillis()
        val lastNotificationTime = MySharedPreferences.getNotificationTimeStamp(this)
        val interval = MySharedPreferences.getNotificationInterval(this)
        val convertedInterval = minutesToMilliseconds(interval)

        // Check if enough time has passed since the last notification
        return now - lastNotificationTime >= convertedInterval
    }

    private fun minutesToMilliseconds(minutes: Int): Long {
        val millisecondsInMinute = 60 * 1000L // 60 seconds * 1000 milliseconds
        return minutes * millisecondsInMinute
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun showNotificationAlert(
        activity: Activity,
        title: String?,
        content: String?,
        icon: ByteArray?,
        isReply: Boolean,
        actions: Array<Notification.Action>
    ) {
        MySharedPreferences.saveNotificationTimeStamp(this, System.currentTimeMillis())
        val iconBitmap = icon?.let { byteArrayToBitmap(it) }
        val notificationLayout = NotificationLayoutBinding.inflate(LayoutInflater.from(this))
        iconBitmap?.let { notificationLayout.appIcon.setImageBitmap(it) }
        title?.let { notificationLayout.title.text = it }
        content?.let { notificationLayout.content.text = it }

        var canHide: Boolean
        Alerter.hide()
        canHide = true
        Alerter.create(activity, R.layout.notification_layout)
            .also { alerter ->
                val layout = alerter.getLayoutContainer()
                if (layout != null) {
                    var isExpanded = false
                    val titleTv: AppCompatTextView = layout.findViewById(R.id.title)
                    val contentTv: AppCompatTextView = layout.findViewById(R.id.content)
                    val iconIv: AppCompatImageView = layout.findViewById(R.id.app_icon)
                    val replyEt: EditText = layout.findViewById(R.id.replyEt)
                    val sendBtn: ImageView = layout.findViewById(R.id.reply_btn)
                    val expandBtn: ImageView = layout.findViewById(R.id.expand)
                    val actionsLayout: LinearLayout = layout.findViewById(R.id.bottomLinearLayout)
                    titleTv.text = title
                    contentTv.text = content
                    iconIv.setImageBitmap(iconBitmap)
                    expandBtn.setOnClickListener {
                        isExpanded = !isExpanded
                        if (isExpanded) {
                            expandBtn.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_up_arrow))
                            contentTv.maxLines = Integer.MAX_VALUE
                        } else {
                            expandBtn.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_down_arrow))
                            contentTv.maxLines = 1
                        }
                    }
                    if (isReply) {
                        replyEt.visibility = View.VISIBLE
                        sendBtn.visibility = View.VISIBLE
                        replyEt.setOnFocusChangeListener { v, hasFocus ->
                            if (hasFocus) {
                                canHide = false
                            }
                        }
                        replyEt.doAfterTextChanged { edit ->
                            edit?.let { text ->
                                if (text.isNotBlank()) {
                                    sendBtn.isClickable = true
                                    sendBtn.setOnClickListener {
                                        NotificationListener.instance?.reply(text.toString())
                                        Alerter.hide()
                                    }
                                } else {
                                    sendBtn.isClickable = false
                                }
                            }
                        }
                    }

                    if (actions.isNotEmpty()) {
                        actions.forEach { action ->
                            Log.e("action", action.actionIntent.toString())
                            if (action.title.toString().equals("reply", true)) return@forEach
                            val button = Button(this)
                            button.text = action.title
                            actionsLayout.addView(button)
                            button.setOnClickListener {
                                val actionIntent = action.actionIntent
                                try {
                                    actionIntent.send()
                                    if (Alerter.isShowing) {
                                        Alerter.hide()
                                    }
                                } catch (e: PendingIntent.CanceledException) {
                                    Log.e("NotificationListener", "Error launching PendingIntent: $e")
                                }
                            }
                        }
                    }
                }
                val alerterScope = SupervisorJob()
                val alerterJob = CoroutineScope(Dispatchers.Main + alerterScope)
                alerter.setOnShowListener {
                    alerterJob.launch {
                        while (canHide) {
                            delay(6000)
                            if (canHide) Alerter.hide()
                        }
                        alerterJob.cancel()
                    }
                }

                alerter.setOnHideListener {
                    alerterJob.cancel()
                }

                alerter.enableInfiniteDuration(true)
                alerter.show()
            }
    }

    private fun byteArrayToBitmap(byteArray: ByteArray): Bitmap? {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }


    private fun listenToUploading() = lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uploadingStatus.collectLatest { result ->
                when(result) {
                    is Resource.Success -> {
                        val title = loadingDialog?.findViewById<TextView>(R.id.title)
                        title?.text = "Loading..."
                        loadingDialog?.dismiss()
                        Toast.makeText(this@BugReportActivity, "Bug has been sent successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    is Resource.Loading -> {
                        loadingDialog?.show()
                    }
                    is Resource.Error -> {
                        loadingDialog?.dismiss()
                        Toast.makeText(this@BugReportActivity, result.message.toString(), Toast.LENGTH_SHORT).show()
                    }

                    else -> {}
                }
            }
        }
    }

    private fun submit() {
        val title = binding.titleEt.text.toString()
        val description = binding.descriptionEt.text.toString()
        bugPhotos?.let { viewModel.bugMedia.addAll(it) }
        viewModel.bugMedia.addAll(compressedVideos)
        viewModel.addBug(title, description)
    }

    private fun showConfirmDialog() {
        val dialog = ConfirmDialog(this)
        dialog.setOnActionListener(object : ConfirmDialog.OnActionListener {
            override fun onOK() {
                submit()
                dialog.dismiss()
            }

            override fun onCancel() {
                dialog.dismiss()
            }
        })
        dialog.show()
    }

    private val pickMultipleMediaItems = registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(5)) { task ->
        if (task.isNotEmpty()) {
            bugMedia = task.toMutableList()
            bugPhotos = task.filter { isImage(it) }.toMutableList()
            bugVideos = task.filter { isVideo(it) }.toMutableList()
            Log.e("bugVideos", bugVideos.toString())
            bugVideos?.let {
                videoCompressor(it)
            }
            bugMedia?.forEach { uri ->

                val imageView = LayoutInflater.from(this)
                    .inflate(R.layout.product_image, binding.imagesLayout, false) as ImageView
                binding.imagesLayout.addView(imageView)
                Glide.with(this).load(uri).into(imageView)
            }
        }
    }

    private fun queryName(resolver: ContentResolver, uri: Uri): String {
        val returnCursor: Cursor = resolver.query(uri, null, null, null, null)!!
        val nameIndex: Int = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        val name: String = returnCursor.getString(nameIndex)
        returnCursor.close()
        return name
    }

    private fun videoCompressor(list: List<Uri>) {
        VideoCompressor.start(
            context = this,
            uris = list,
            isStreamable = false,
            sharedStorageConfiguration = SharedStorageConfiguration(
                saveAt = SaveLocation.movies, // => default is movies
                subFolderName = "my-videos" // => optional
            ),
            configureWith = Configuration(
                quality = VideoQuality.HIGH,
                videoNames = list.map { queryName(contentResolver, it) },
                isMinBitrateCheckEnabled = false
            ),
            listener = object : CompressionListener {
                override fun onCancelled(index: Int) {
                    loadingDialog?.dismiss()
                }

                override fun onFailure(index: Int, failureMessage: String) {
                    loadingDialog?.dismiss()
                    Log.e("failure", failureMessage)
                }

                override fun onProgress(index: Int, percent: Float) {
                    runOnUiThread {
                        loadingPercent = percent.toInt()
                        val title = loadingDialog?.findViewById<TextView>(R.id.title)
                        title?.text = "Loading $loadingPercent"
                        if (loadingDialog?.isShowing != true) {
                            loadingDialog?.show()
                        }
                    }

                    Log.e("progress", percent.toString())
                }

                override fun onStart(index: Int) {
                    Log.e("start", "index: $index")
                }

                override fun onSuccess(index: Int, size: Long, path: String?) {
                    path?.let {
                        val file = File(path)
                        compressedVideos.add(Uri.fromFile(file))
                    }
                    loadingDialog?.dismiss()
                    Log.e("compress", "index: $index, path: $path")
                }
            }
        )
    }

    private fun isVideo(uri: Uri): Boolean {
        val contentResolver = contentResolver
        val mimeType = contentResolver.getType(uri)
        return mimeType != null && mimeType.startsWith("video/")
    }

    private fun isImage(uri: Uri): Boolean {
        val contentResolver = contentResolver
        val mimeType = contentResolver.getType(uri)
        return mimeType != null && mimeType.startsWith("image/")
    }

    private fun hideSystemUI() {
        val decorView = window.decorView
        val uiOptions = (
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
        decorView.systemUiVisibility = uiOptions
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private fun enableFullScreenMode() {
        val window = window
        val insetsController = window.insetsController
        if (insetsController != null) {
            // Hide both the navigation bar and status bar.
            insetsController.hide(WindowInsets.Type.systemBars())
            // Make the content appear behind the cutout (notch) if present.
            insetsController.systemBarsBehavior = WindowInsetsController.BEHAVIOR_DEFAULT
        }
    }
}