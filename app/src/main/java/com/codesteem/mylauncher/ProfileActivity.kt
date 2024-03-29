package com.codesteem.mylauncher

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Notification
import android.app.PendingIntent
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.provider.Settings
import android.telephony.PhoneStateListener
import android.telephony.SignalStrength
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.widget.doAfterTextChanged
import androidx.documentfile.provider.DocumentFile
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.Lifecycle
import com.bumptech.glide.Glide
import com.codesteem.mylauncher.bug_report.BugReportActivity
import com.codesteem.mylauncher.databinding.ActivityProfileBinding
import com.codesteem.mylauncher.databinding.NotificationLayoutBinding
import com.codesteem.mylauncher.databinding.PopupEdittextLayoutBinding
import com.codesteem.mylauncher.models.AppInfo
import com.codesteem.mylauncher.models.NotificationsModel
import com.codesteem.mylauncher.models.UserInfo
import com.codesteem.mylauncher.test.Month
import com.codesteem.mylauncher.util.MySharedPreferences
import com.codesteem.mylauncher.util.MySharedPreferences.KEY_DISCORD_NOW
import com.codesteem.mylauncher.util.MySharedPreferences.KEY_EMAIL
import com.codesteem.mylauncher.util.MySharedPreferences.KEY_INSTAGRAM
import com.codesteem.mylauncher.util.MySharedPreferences.KEY_MESSENGER
import com.codesteem.mylauncher.util.MySharedPreferences.KEY_PHONE
import com.codesteem.mylauncher.util.MySharedPreferences.KEY_SNAPCHAT
import com.codesteem.mylauncher.util.MySharedPreferences.KEY_USER_IMAGE
import com.codesteem.mylauncher.util.MySharedPreferences.KEY_USER_NAME
import com.codesteem.mylauncher.util.MySharedPreferences.KEY_WHATSAPP
import com.codesteem.mylauncher.util.MySharedPreferences.KEY_X
import com.codesteem.mylauncher.util.MySharedPreferences.getDataItem
import com.codesteem.mylauncher.util.MySharedPreferences.saveDataItem
import com.codesteem.mylauncher.util.MySharedPreferences.saveUserInfo
import com.codesteem.mylauncher.util.NotificationListener
import com.example.zhouwei.library.CustomPopWindow
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.skydoves.powermenu.CustomPowerMenu
import com.tapadoo.alerter.Alerter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.Objects


class ProfileActivity : AppCompatActivity() {
    private var bottomSheetPhotoPicker: BottomSheetDialog? = null
    private val galleryPermissionsReqCode = 1
    private val galleryContentReqCode = 2
    private val cameraPermissionsReqCode = 3
    private val cameraReqCode = 4
    private var currentPhotoPath = ""
    private var photoFile: File? = null
    private var photoURI: Uri? = null
    lateinit var iv_thumbnail: ImageView
    private lateinit var binding: ActivityProfileBinding
    private lateinit var timeHandler: Handler
    private lateinit var telephonyManager: TelephonyManager

    private var selectedPlatforms = ArrayList<Share>()

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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
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
        telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        iv_thumbnail=findViewById(R.id.iv_thumbnail)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            enableFullScreenMode()
        }
        hideSystemUI()
        timeHandler = Handler()
        findViewById<ImageView>(R.id.back).setOnClickListener {

            saveData()
//            val intent = Intent(applicationContext, MainActivity::class.java)
//            startActivity(intent)
//            finishAffinity()
            finish()
        }
        iv_thumbnail.setOnClickListener {
            photoPickerBottomSheet()
        }
        retrieveAndSetText()

        val userImage = File(getDataItem(KEY_USER_IMAGE, this))

        Glide.with(this@ProfileActivity)
            .load(userImage)
            .error(R.drawable.new_avatar)
            .into(iv_thumbnail)

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
                val isActivityInForeground = this@ProfileActivity.lifecycle.currentState.isAtLeast(
                    Lifecycle.State.RESUMED
                )
                val actions = intent.getParcelableArrayExtra("actions", Notification.Action::class.java)
                if (canShowNotification() && isActivityInForeground) {
                    showNotificationAlert(this@ProfileActivity, title, text, smallIconByteArray, isReply, actions!!)
                }
            }
        }
    }
    private fun saveData(){
        /*val userInfo = UserInfo(
            currentPhotoPath,
            binding.tvName.text.toString(),
            binding.etPhone.text.toString(),
            binding.etEmail.text.toString(),
            binding.etWsp.text.toString(),
            binding.etInsta.text.toString(),
            binding.etMessenger.text.toString(),
            binding.etSnap.text.toString(),
            binding.etX.text.toString(),
            binding.etDiscord.text.toString()
        )

        saveUserInfo(this, userInfo)*/

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

    private fun checkOpener(view: View) {
        view.setOnClickListener {
            //checkBox.visibility = View.VISIBLE
            //checkBox.isChecked = true
            binding.doneBtn.visibility = View.VISIBLE
            showCheckBox(binding.phoneCheckbox)
            showCheckBox(binding.emailCheckbox)
            showCheckBox(binding.instagramCheckbox)
            showCheckBox(binding.whatsappCheckbox)
            showCheckBox(binding.messengerCheckbox)
            showCheckBox(binding.snapCheckbox)
            showCheckBox(binding.discordCheckbox)
            showCheckBox(binding.twitterCheckbox)
        }
    }

    private fun showCheckBox(checkBox: CheckBox) {
        checkBox.visibility = View.VISIBLE
    }

    private fun hideCheckBox(checkBox: CheckBox) {
        checkBox.visibility = View.INVISIBLE
    }

    private fun openEditTextPopup(view: View, key: String) {
        val layout = PopupEdittextLayoutBinding.inflate(layoutInflater)
        val data = getDataItem(key, this)
        layout.popupEt.setText(data)
        layout.popupEt.doAfterTextChanged { edit ->
            edit?.let {
                saveDataItem(key, it.toString(), this)
            }
        }
        val popup = CustomPopWindow.PopupWindowBuilder(this)
            .size(800, 250)
            .setView(layout.root)
        popup.create().showAsDropDown(view)
    }

    fun appendText(currentText: String?, newText: String): String {
        return if (!currentText.isNullOrBlank()) {
            // If there is existing text, add the new text on a new line
            "$currentText\n$newText"
        } else {
            // If there is no existing text, just use the new text
            newText
        }
    }
    private fun retrieveAndSetText() {
        binding.tvName.doAfterTextChanged { edit ->
            edit?.let {
                saveDataItem(KEY_USER_NAME, it.toString(), this)
            }
        }
        binding.phone.setOnClickListener {
            openEditTextPopup(it, KEY_PHONE)
        }
        binding.gmail.setOnClickListener {
            openEditTextPopup(it, KEY_EMAIL)
        }
        binding.instagram.setOnClickListener {
            openEditTextPopup(it, KEY_INSTAGRAM)
        }
        binding.whatsapp.setOnClickListener {
            openEditTextPopup(it, KEY_WHATSAPP)
        }
        binding.messenger.setOnClickListener {
            openEditTextPopup(it, KEY_MESSENGER)
        }
        binding.snapchat.setOnClickListener {
            openEditTextPopup(it, KEY_SNAPCHAT)
        }
        binding.discord.setOnClickListener {
            openEditTextPopup(it, KEY_DISCORD_NOW)
        }
        binding.twitter.setOnClickListener {
            openEditTextPopup(it, KEY_X)
        }

        /*checkOpener(binding.phone, binding.phoneCheckbox)
        checkOpener(binding.gmail, binding.emailCheckbox)
        checkOpener(binding.instagram, binding.instagramCheckbox)
        checkOpener(binding.whatsapp, binding.whatsappCheckbox)
        checkOpener(binding.messenger, binding.messengerCheckbox)
        checkOpener(binding.snapchat, binding.snapCheckbox)
        checkOpener(binding.discord, binding.discordCheckbox)
        checkOpener(binding.twitter, binding.twitterCheckbox)*/

        addShareItem("Name:", KEY_USER_NAME)
        addShareItem("Phone:", KEY_PHONE)
        addShareItem("Email:", KEY_EMAIL)
        addShareItem("Instagram:", KEY_INSTAGRAM)
        addShareItem("WhatsApp:", KEY_WHATSAPP)
        addShareItem("Messenger:", KEY_MESSENGER)
        addShareItem("Snapchat:", KEY_SNAPCHAT)
        addShareItem("Discord:", KEY_DISCORD_NOW)
        addShareItem("Twitter(X):", KEY_X)
        binding.phoneCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                selectedPlatforms.add(Share("Phone:", getDataItem(KEY_PHONE, this)))
            } else {
                selectedPlatforms.remove(Share("Phone:", getDataItem(KEY_PHONE, this)))
            }
        }
        binding.emailCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                selectedPlatforms.add(Share("Email:", getDataItem(KEY_EMAIL, this)))
            } else {
                selectedPlatforms.remove(Share("Email:", getDataItem(KEY_EMAIL, this)))
            }
        }
        binding.instagramCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                selectedPlatforms.add(Share("Instagram:", getDataItem(KEY_INSTAGRAM, this)))
            } else {
                selectedPlatforms.remove(Share("Instagram:", getDataItem(KEY_INSTAGRAM, this)))
            }
        }
        binding.whatsappCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                selectedPlatforms.add(Share("WhatsApp:", getDataItem(KEY_WHATSAPP, this)))
            } else {
                selectedPlatforms.remove(Share("WhatsApp:", getDataItem(KEY_WHATSAPP, this)))
            }
        }
        binding.messengerCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                selectedPlatforms.add(Share("Messenger:", getDataItem(KEY_MESSENGER, this)))
            } else {
                selectedPlatforms.remove(Share("Messenger:", getDataItem(KEY_MESSENGER, this)))
            }
        }
        binding.snapCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                selectedPlatforms.add(Share("Snapchat:", getDataItem(KEY_SNAPCHAT, this)))
            } else {
                selectedPlatforms.remove(Share("Snapchat:", getDataItem(KEY_SNAPCHAT, this)))
            }
        }
        binding.discordCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                selectedPlatforms.add(Share("Discord:", getDataItem(KEY_DISCORD_NOW, this)))
            } else {
                selectedPlatforms.remove(Share("Discord:", getDataItem(KEY_DISCORD_NOW, this)))
            }
        }
        binding.twitterCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                selectedPlatforms.add(Share("Twitter(X):", getDataItem(KEY_X, this)))
            } else {
                selectedPlatforms.remove(Share("Twitter(X):", getDataItem(KEY_X, this)))
            }
        }
        binding.shareBtn.apply {
            checkOpener(this)
        }

        binding.doneBtn.setOnClickListener {

            var shareText = ""
            selectedPlatforms.forEach { platform ->
                shareText = appendText(shareText, "${platform.app} ${platform.data}")
            }
            Log.e("shareText", shareText)
            if (shareText.isNotBlank()) {
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, shareText)
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }
            binding.doneBtn.visibility = View.INVISIBLE
            hideCheckBox(binding.phoneCheckbox)
            hideCheckBox(binding.emailCheckbox)
            hideCheckBox(binding.instagramCheckbox)
            hideCheckBox(binding.whatsappCheckbox)
            hideCheckBox(binding.messengerCheckbox)
            hideCheckBox(binding.snapCheckbox)
            hideCheckBox(binding.discordCheckbox)
            hideCheckBox(binding.twitterCheckbox)
        }
        val retrievedUserInfo = MySharedPreferences.getUserInfo(this)

        if (retrievedUserInfo != null) {
            // Set the retrieved data to corresponding TextViews
            if (retrievedUserInfo.getUserName()!=""){
                binding.tvName.setText(retrievedUserInfo.getUserName())

            }else{
                binding.tvName.setText("Your name")
            }
            // Load and display user image using Glide
            // Load and display user image using Glide with a placeholder
            Glide.with(this)
                .load(retrievedUserInfo.userImage)
                .placeholder(R.drawable.new_avatar) // Placeholder image resource
                .into(binding.ivThumbnail)
            currentPhotoPath=retrievedUserInfo.userImage
        }
    }

    private fun addShareItem(platformName: String, key: String) {
        selectedPlatforms.add(Share(platformName, getDataItem(key, this)))
    }


    private fun photoPickerBottomSheet() {
        if (bottomSheetPhotoPicker == null)
            bottomSheetPhotoPicker =
                BottomSheetDialog(this@ProfileActivity, R.style.BottomSheetTransparentBgTheme)
        val view =
            this.layoutInflater.inflate(
                R.layout.bottom_sheet_photo_picker,
                null
            )
        view.findViewById<RelativeLayout>(R.id.rlParent)!!.setOnClickListener { v ->
            hideKeyBoard(v, this@ProfileActivity!!)
        }
        val tvPhotoGallery = view.findViewById<TextView>(R.id.tvPhotoGallery)
        val tvCamera = view.findViewById<TextView>(R.id.tvCamera)
        val tvCancel = view.findViewById<TextView>(R.id.tvCancel)
        tvPhotoGallery.setOnClickListener {
            if (checkMediaPermissions(this@ProfileActivity))
                askForMediaPermissions(this@ProfileActivity!!, galleryPermissionsReqCode)
            else {
                openGallery()
                bottomSheetPhotoPicker?.dismiss()
            }
        }
        tvCamera.setOnClickListener {
            if (checkCameraPermissions(this@ProfileActivity!!))
                askForCameraPermissions(this@ProfileActivity!!, cameraPermissionsReqCode)
            else {
                openCamera()
                bottomSheetPhotoPicker?.dismiss()
            }
        }
        tvCancel.setOnClickListener {
            bottomSheetPhotoPicker?.dismiss()
        }
        bottomSheetPhotoPicker?.setCancelable(false)
        bottomSheetPhotoPicker?.setCanceledOnTouchOutside(false)
        bottomSheetPhotoPicker?.setContentView(view)
        bottomSheetPhotoPicker?.show()
    }
    fun checkMediaPermissions(activity: Activity): Boolean {
        return if (Build.VERSION.SDK_INT >= 33)
            ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.READ_MEDIA_IMAGES
            ) != PackageManager.PERMISSION_GRANTED
        else
            ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
    }
    fun askForMediaPermissions(activity: Activity, permissionCode: Int) {
        return if (Build.VERSION.SDK_INT >= 33)
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                permissionCode
            )
        else
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                permissionCode
            )
    }
    fun hideKeyBoard(view: View, activity: Activity) {
        val imm =
            activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.applicationWindowToken, 0)
    }
    // Request Code is used to check which permission called this function.
    // This request code is provided when the user is prompt for permission.
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            galleryPermissionsReqCode ->                 // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery()
                } else {
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                }
        }
    }
    fun checkCameraPermissions(activity: Activity): Boolean {
        return ActivityCompat.checkSelfPermission(
            activity,
            Manifest.permission.CAMERA
        ) != PackageManager.PERMISSION_GRANTED
    }

    fun askForCameraPermissions(activity: Activity, permissionCode: Int) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
                Manifest.permission.CAMERA
            ),
            permissionCode
        )
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            cameraReqCode -> if (resultCode === Activity.RESULT_OK) {
                try {
                    var bitmap = MediaStore.Images.Media.getBitmap(
                        contentResolver,
                        Uri.fromFile(File(currentPhotoPath))
                    )
                    var rotatedBitmap: Bitmap? = modifyOrientation(bitmap, currentPhotoPath)
                    Glide.with(this@ProfileActivity!!)
                        .load(rotatedBitmap)
                        .into(iv_thumbnail)

                    Log.e("photoPath", currentPhotoPath)
                    saveDataItem(KEY_USER_IMAGE, currentPhotoPath, this)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            galleryContentReqCode -> if (resultCode === Activity.RESULT_OK) {
                try {
                    val uri = data?.data
                    val documentFile = DocumentFile.fromSingleUri(
                        this@ProfileActivity!!,
                        data?.data!!
                    )
                    val fileName = documentFile!!.name
                    val contentResolver: ContentResolver =
                        contentResolver
                    val filePath: String =
                        (applicationInfo?.dataDir.toString() + File.separator
                                + System.currentTimeMillis() + fileName!!.substring(
                            fileName!!.lastIndexOf(
                                "."
                            )
                        ))
                    saveDataItem(KEY_USER_IMAGE, filePath, this)
                    val file = File(filePath)
                    try {
                        val inputStream = contentResolver.openInputStream(uri!!)
                        val outputStream: OutputStream = FileOutputStream(file)
                        val buf = ByteArray(1024)
                        var len: Int
                        while (inputStream!!.read(buf).also { len = it } > 0) outputStream.write(
                            buf,
                            0,
                            len
                        )
                        outputStream.close()
                        inputStream!!.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    currentPhotoPath=filePath
                    var rotatedBitmap =
                        modifyOrientation(BitmapFactory.decodeFile(filePath), filePath)
                    Glide.with(this@ProfileActivity!!)
                        .load(rotatedBitmap)
                        .into(iv_thumbnail)
                } catch (e: Exception) {
                    e.printStackTrace()

                }
            }
        }

    }
    @Throws(IOException::class)
    fun modifyOrientation(bitmap: Bitmap, image_absolute_path: String?): Bitmap? {
        val ei = ExifInterface(
            image_absolute_path!!
        )
        return when (ei.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotate(bitmap, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotate(bitmap, 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotate(bitmap, 270f)
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> flip(
                bitmap,
                horizontal = true,
                vertical = false
            )

            ExifInterface.ORIENTATION_FLIP_VERTICAL -> flip(
                bitmap,
                horizontal = false,
                vertical = true
            )

            else -> bitmap
        }
    }

    private fun rotate(bitmap: Bitmap, degrees: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(degrees)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun flip(bitmap: Bitmap, horizontal: Boolean, vertical: Boolean): Bitmap? {
        val matrix = Matrix()
        matrix.preScale(
            (if (horizontal) -1 else 1.toFloat()) as Float,
            (if (vertical) -1 else 1.toFloat()) as Float
        )
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        val chooser = Intent.createChooser(
            intent,
           resources?.getString(R.string.choose_profile_picture)
        )
        startActivityForResult(chooser, galleryContentReqCode)
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        photoFile = try {
            createImageFile()
        } catch (ex: IOException) {
            null
        }
        if (photoFile !== null) {
            photoURI = FileProvider.getUriForFile(
                Objects.requireNonNull(this@ProfileActivity),
                "com.codesteem.mylauncher" + ".provider",
                photoFile!!
            )
        }
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        startActivityForResult(
            cameraIntent, cameraReqCode
        )
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val storageDir: File =
            getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "my_image",
            ".png",
            storageDir
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
//            authViewModel?.signUpMainModel?.mediaFilePath = currentPhotoPath
//            authViewModel?.signUpMainModel?.mediaFileName = getFileName(currentPhotoPath)
//            authViewModel?.signUpMainModel?.mediaFileType =
//                "image/" + getFileType(currentPhotoPath)
        }
    }

    private fun hideSystemUI() {
        val decorView = window.decorView
        val uiOptions = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        decorView.systemUiVisibility = uiOptions
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }
}