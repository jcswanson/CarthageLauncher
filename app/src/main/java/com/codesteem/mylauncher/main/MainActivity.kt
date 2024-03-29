package com.codesteem.mylauncher.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Notification
import android.app.role.RoleManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.ContactsContract
import android.provider.Settings
import android.provider.Telephony
import android.service.notification.StatusBarNotification
import android.telecom.TelecomManager
import android.telephony.TelephonyManager
import android.text.method.ScrollingMovementMethod
import android.util.Base64
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.GravityCompat
import androidx.core.widget.doAfterTextChanged
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codesteem.mylauncher.MenuActivity
import com.codesteem.mylauncher.ProfileActivity
import com.codesteem.mylauncher.R
import com.codesteem.mylauncher.Resource
import com.codesteem.mylauncher.ThemeSettings
import com.codesteem.mylauncher.adapter.AppSearchAdapter
import com.codesteem.mylauncher.adapter.NotificationsAdapter
import com.codesteem.mylauncher.bug_report.BugReportActivity
import com.codesteem.mylauncher.database.NotificationEntity
import com.codesteem.mylauncher.databinding.ActivityMainBinding
import com.codesteem.mylauncher.models.AppInfo
import com.codesteem.mylauncher.models.AppSearchModel
import com.codesteem.mylauncher.models.NotificationsModel
import com.codesteem.mylauncher.util.ItemTouchHelperCallback
import com.codesteem.mylauncher.util.MySharedPreferences
import com.codesteem.mylauncher.util.NotificationListener
import com.codesteem.mylauncher.util.copyToClipboard
import com.codesteem.mylauncher.util.hideKeyboard
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.showAlignBottom
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NotificationsAdapter.OnItemDeleteListener {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private var hiddenApps: ArrayList<String> = ArrayList()
    private lateinit var appSearchModelsList: java.util.ArrayList<AppSearchModel>

    private lateinit var notificationsAdapter: NotificationsAdapter
    private lateinit var data: ArrayList<NotificationEntity>

    private val PERMISSION_REQUEST_CODE = 1001
    private var packageManager: PackageManager? = null

    private lateinit var dialogAdapter: AppSearchAdapter
    private lateinit var editText: EditText
    private var installedApps: MutableList<ApplicationInfo>? = null
    private lateinit var showCallLogLauncher: ActivityResultLauncher<Intent>
    var msgCounter=0
    var allCounter=0
    private lateinit var telephonyManager: TelephonyManager

    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private var activities: List<ComponentName> = ArrayList()

    private val notificationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "ACTION_NOTIFICATION_RECEIVED") {
                val isActivityInForeground = this@MainActivity.lifecycle.currentState.isAtLeast(
                    Lifecycle.State.RESUMED
                )
                if (canShowNotification() && isActivityInForeground) {
                    refreshData()
                }
                setupCallsBubble()
                setupMessagingBubble()
            }
        }
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

    override fun onStart() {
        super.onStart()
        Log.e("mainActivity", "onStart")
        if (canShowNotification()) {
            refreshData()
        }

    }

    override fun onPostResume() {
        super.onPostResume()
        Log.e("mainActivity", "onPostResume")
        binding.navMenuDrawer.priorityModeSwitch.isChecked = MySharedPreferences.getPriorityMode(this)
        if (canShowNotification()) {
            refreshData()
        }
    }

    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val message = intent.getBooleanExtra("Status", false)
            if (canShowNotification()) {
                refreshData()
            }
            Log.e("message", message.toString())
        }
    }

    private fun subscribeForPerplexity() = lifecycleScope.launch {
        viewModel.askPerplexityState.collectLatest { result ->
            when(result) {
                is Resource.Success -> {
                    result.data?.let { perplexityResponse ->
                        val content = perplexityResponse.choices.firstOrNull()
                        content?.let {
                            if (it.index == 0) {
                                if (it.finish_reason == "stop") {
                                    searchCopyBallon()
                                }
                                if (it.message.content.isNotBlank()) {
                                    binding.webSearchResult.text = it.message.content
                                }
                            }
                        }
                    }
                }

                else -> {}
            }
        }
    }

    private fun subscribeForDeleteNotification() = lifecycleScope.launch {
        viewModel.deleteNotificationsState.collectLatest { result ->
            when(result) {
                is Resource.Success -> {
                    if (canShowNotification()) {
                        refreshData()
                    }
                }
                else -> {}
            }
        }
    }

    private fun subscribeForAllNotifications() = lifecycleScope.launch {
        viewModel.allNotificationsState.collectLatest { notifications ->
            data = ArrayList(notifications)
            notificationsAdapter.submitList(data)
            //notificationsAdapter.notifyDataSetChanged()
            if (data.isEmpty()) {
                binding.tvNoNoti.visibility = View.VISIBLE
            } else {
                binding.tvNoNoti.visibility = View.GONE
            }
        }
    }

    private fun searchCopyBallon() {
        val balloon = Balloon.Builder(this)
            .setWidthRatio(1.0f)
            .setHeight(BalloonSizeSpec.WRAP)
            .setText("Click on the search result to copy")
            .setTextColorResource(R.color.black)
            .setTextSize(15f)
            .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
            .setArrowSize(10)
            .setArrowPosition(0.5f)
            .setPadding(12)
            .setCornerRadius(8f)
            .setBackgroundColorResource(R.color.pink)
            .setBalloonAnimation(BalloonAnimation.ELASTIC)
            .setLifecycleOwner(this)
            .build()
        balloon.setOnBalloonDismissListener {
            MySharedPreferences.saveSearchCopyBallon(this)
        }
        binding.searchResultScrollView.showAlignBottom(balloon)
        if (!MySharedPreferences.isSearchCopBallonShown(this)) {
            binding.searchResultScrollView.showAlignBottom(balloon)
        }
    }

    private fun refreshData() {
        if (isNotificationServiceEnabled()) {
            if (MySharedPreferences.getPriorityMode(this)) {
                viewModel.getPrioritizedNotifications()
            } else {
                viewModel.getNotifications()
            }
            MySharedPreferences.saveNotificationTimeStamp(this@MainActivity, System.currentTimeMillis())
            val myData = NotificationListener.instance?.getNotifications()
            if (myData != null) {
                val newData = ArrayList<NotificationsModel>()
                myData.forEach { newData.add(getNotiData(it)) }
                handleEmptyNotifications(data)
                if (newData.isEmpty()) {
                    binding.tvNoNoti.visibility = View.VISIBLE
                } else {
                    binding.tvNoNoti.visibility = View.GONE
                }
            }
        }
    }

    private fun handleEmptyNotifications(notifications: ArrayList<NotificationEntity>) {
        if (notifications.isEmpty()) {
            binding.deleteCard.visibility = View.GONE
            binding.tvNoNoti.visibility = View.VISIBLE
        } else {
            binding.deleteCard.visibility = View.VISIBLE
            binding.tvNoNoti.visibility = View.GONE
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        setContentView(binding.root)
        appDrawer()
        lightDarkMode()
        showLauncherSelection()
        LocalBroadcastManager.getInstance(this).registerReceiver(
            mMessageReceiver, IntentFilter("NOTI"))
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "mainHomeScreen")
        }
        editText=binding.editText
        telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        askForSomePermission()
        askingForManagingStorage()
        if (!isNotificationServiceEnabled()) {
            val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
            startActivityForResult(intent, PERMISSION_REQUEST_CODE)
        }
        val intentFilter = IntentFilter("ACTION_NOTIFICATION_RECEIVED")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(notificationReceiver, intentFilter, RECEIVER_EXPORTED)
        } else {
            registerReceiver(notificationReceiver, intentFilter)
        }

        setData()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            enableFullScreenMode()
        }
        hideSystemUI()
        initData()
        clickListeners()
        subscribeForAllNotifications()
        subscribeForDeleteNotification()
        subscribeForPerplexity()

        showCallLogLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            Log.d("TAG", result.toString())
        }
    }

    private val somePermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
    }

    private fun askForSomePermission() {
        val permissions = mutableListOf(
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA
        )
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        somePermissions.launch(
            permissions.toTypedArray()
        )
    }

    private fun askingForManagingStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                val storageIntent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                startActivity(storageIntent)
            }
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

    private fun initData() {
        packageManager = getPackageManager()
        installedApps = packageManager!!.getInstalledApplications(PackageManager.GET_META_DATA)
        val recyclerView: RecyclerView = binding.appRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        appSearchModelsList = ArrayList<AppSearchModel>()
        dialogAdapter = AppSearchAdapter(this@MainActivity, binding.cvSearchbar,binding.editText)
        recyclerView.adapter = dialogAdapter
        for (appInfo in installedApps!!){
            val appTitle = packageManager!!.getApplicationLabel(appInfo).toString()
            val packageName = appInfo.packageName

            val appLogo = getPackageManager().getApplicationIcon(appInfo)
            if (getPackageManager().getLaunchIntentForPackage(appInfo.packageName) != null){
                appSearchModelsList.add(AppSearchModel(appTitle,appLogo,packageName))
            }
        }

        editText.doAfterTextChanged { text ->
            if (text?.isBlank() == true){
                recyclerView.visibility=View.GONE
                binding.webSearchButton.visibility = View.GONE
            } else {
                recyclerView.visibility=View.VISIBLE
                binding.webSearchButton.visibility = View.VISIBLE
                val searchText = text.toString()
                updateMatchingApps(searchText)
            }
        }

        binding.searchClearBtn.setOnClickListener {
            binding.cvSearchbar.visibility = View.GONE
            editText.text.clear()
            hideKeyboard(it)
        }

        binding.webSearchButton.setOnClickListener {
            showPerplexity()
            val searchText = editText.text.toString()
            if (!editText.text.isNullOrBlank()) {
                viewModel.askPerplexity(searchText)
            }
        }
        binding.closeBtn.setOnClickListener {
            hidePerplexity()
        }
    }

    private fun showPerplexity() {
        binding.appRecyclerView.visibility = View.GONE
        binding.deleteCard.visibility = View.GONE
        binding.rv.visibility = View.GONE
        binding.tvNoNoti.visibility = View.GONE
        binding.webSearchResultCard.visibility = View.VISIBLE
        binding.webSearchButton.visibility = View.GONE
        binding.webSearchResult.movementMethod = ScrollingMovementMethod()
        binding.webSearchResult.setOnClickListener {
            copyToClipboard(binding.webSearchResult.text.toString(), this)
        }
    }

    private fun hidePerplexity() {
        refreshData()
        binding.webSearchResultCard.visibility = View.GONE
        binding.appRecyclerView.visibility=View.GONE
        binding.cvSearchbar.visibility=View.GONE
        editText.text.clear()
    }

    private val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { activityResult ->
        Log.e("launcherResult", activityResult.toString())
        if (activityResult.resultCode == Activity.RESULT_OK) {
        } else {
        }
    }

    private fun showLauncherSelection() {
        val roleManager = getSystemService(Context.ROLE_SERVICE) as? RoleManager
        if (roleManager != null && roleManager.isRoleAvailable(RoleManager.ROLE_HOME) &&
            !roleManager.isRoleHeld(RoleManager.ROLE_HOME)
        ) {
            val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_HOME)
            startForResult.launch(intent)
        } else {
            if (!isMyAppLauncherDefault()) {
                val settingsIntent = Intent("android.settings.HOME_SETTINGS")
                launcherSettingsIntent.launch(settingsIntent)
            }
            Log.e("selection", "handled already")
            // Log or handle the case where the role is not available or already held
        }
    }

    private val launcherSettingsIntent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
        } else {
            showLauncherSelection()
        }
    }


    private fun clickListeners() {
        binding.ivThumbnail.setOnClickListener {
            val intent = Intent(applicationContext, ProfileActivity::class.java)
            startActivity(intent)
        }
        binding.tvDeleteAll.setOnClickListener {
            viewModel.deleteAllNotifications()
        }
        binding.ivHome.setOnClickListener {
            allCounter=0

            binding.cvSearchbar.visibility=View.GONE
            binding.allCounter.visibility=View.GONE

            val intent = Intent(applicationContext, MenuActivity::class.java)
            startActivity(intent)
        }
        binding.ivCalls.setOnClickListener {
            binding.cvSearchbar.visibility=View.GONE
            binding.callsCounter.visibility=View.GONE
            openCallApp(applicationContext)
        }
        binding.ivSearch.setOnClickListener {
            binding.cvSearchbar.visibility=View.VISIBLE
            binding.editText.requestFocus()
            showKeyboard(view = binding.editText)

        }
        binding.ivMessages.setOnClickListener {
            binding.cvSearchbar.visibility=View.GONE
            msgCounter=0
            binding.msgCounter.visibility=View.GONE
            openMessagesApp()
        }

    }

    private fun isMyAppLauncherDefault(): Boolean {
        val filter = IntentFilter(Intent.ACTION_MAIN)
        filter.addCategory(Intent.CATEGORY_HOME)
        val filters: MutableList<IntentFilter> = ArrayList()
        filters.add(filter)
        val myPackageName = packageName

        if (packageManager != null) {
            packageManager?.getPreferredActivities(filters, activities, null)
            for (activity in activities) {
                if (myPackageName == activity.packageName) {
                    return true
                }
            }
        }

        return false
    }

    private fun showKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
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

    private fun setData() {
        data = ArrayList()
        data.clear()
        binding.rv.layoutManager = LinearLayoutManager(this)
        notificationsAdapter = NotificationsAdapter(this)
        binding.rv.adapter = notificationsAdapter
        notificationsAdapter.submitList(data.toList())
        notificationsAdapter.setOnNotificationClickListener {
            it.appInfo?.appName?.let { appName ->
                viewModel.deleteNotification(it.id)
                val packageName = getPackageNameByName(this, appName)
                packageName?.let {
                    val appIntent = packageManager?.getLaunchIntentForPackage(it)
                    startActivity(appIntent)
                }
            }
        }

        notificationsAdapter.setOnActionClickListener {
            viewModel.deleteNotification(it.id)
        }

        val itemTouchHelperCallback = ItemTouchHelperCallback(notificationsAdapter)
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.rv)
        if (data.size == 0) {
            binding.tvNoNoti.visibility = View.VISIBLE
        } else {
            binding.tvNoNoti.visibility = View.GONE
        }
    }

    private fun getPackageNameByName(context: Context, appName: String): String? {
        val packageManager: PackageManager = context.packageManager
        val installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

        for (appInfo in installedApps) {
            val label = packageManager.getApplicationLabel(appInfo).toString()
            if (label == appName) {
                return appInfo.packageName
            }
        }

        return null
    }

    override fun onItemDeleted(position: Int, item: NotificationEntity) {
        data.remove(item)
        notificationsAdapter.notifyItemRemoved(position)
        viewModel.deleteNotification(item.id)
        val find = NotificationListener.instance?.myNotifications?.find {
            it.key == item.id
        }
        NotificationListener.instance?.myNotifications?.remove(find)
        handleEmptyNotifications(data)
        if (data.isEmpty()) {
            binding.tvNoNoti.visibility = View.VISIBLE
        }
        setupCallsBubble()
        setupMessagingBubble()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(notificationReceiver)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (isNotificationServiceEnabled()) {
            } else {
                Toast.makeText(
                    this,
                    "Permission denied. The app will not capture notifications.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun openCallApp(context: Context) {
        val googleDialerShowRecent = Intent("com.android.phone.action.RECENT_CALLS")
        if (context.packageManager.queryIntentActivities(
                googleDialerShowRecent,
                PackageManager.MATCH_ALL
            ).size > 0
        ) {
            showCallLogLauncher.launch(googleDialerShowRecent)
        }
    }

    private fun setupCallsBubble() {
        val manger = getSystemService(TELECOM_SERVICE) as TelecomManager
        val name = manger.defaultDialerPackage
        val findFirst = NotificationListener.instance?.myNotifications?.find {
            (it.packageName == name)
        }
        if (findFirst != null) {
            binding.callsCounter.visibility = View.VISIBLE
        } else {
            binding.callsCounter.visibility = View.GONE
        }
    }

    private fun setupMessagingBubble() {
        val defaultSmsApp = Telephony.Sms.getDefaultSmsPackage(this)
        val findFirst = NotificationListener.instance?.myNotifications?.find {
            (it.packageName == defaultSmsApp)
        }
        if (findFirst != null) {
            binding.msgCounter.visibility = View.VISIBLE
        } else {
            binding.msgCounter.visibility = View.GONE
        }
    }

    private fun openMessagesApp() {
        val defaultSmsApp = Telephony.Sms.getDefaultSmsPackage(this)
        launchApp(defaultSmsApp)
    }

    private fun launchApp(packageName: String) {
        val intent = packageManager?.getLaunchIntentForPackage(packageName)
        intent?.let {
            MySharedPreferences.modifyPair(this, packageName)
            startActivity(it)
        } ?: run {
            Toast.makeText(applicationContext, "App not launchable", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateMatchingApps(searchText: String) {
        val filteredApps = appSearchModelsList.filter {
            val trimmedTitle = it.title?.trim()
            trimmedTitle?.lowercase(Locale.ROOT)?.startsWith(searchText.trim().lowercase(Locale.ROOT)) ?: false
        }
        dialogAdapter.setApps(filteredApps)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val view = currentFocus
        if (view != null && (ev.action == MotionEvent.ACTION_UP || ev.action == MotionEvent.ACTION_MOVE) && view is EditText && !view.javaClass.name.startsWith(
                "android.webkit."
            )
        ) {
            if (editText.text?.isNotBlank() == true) {
                //binding.appRecyclerView.visibility=View.GONE
            } else {
                binding.cvSearchbar.visibility=View.GONE
                binding.appRecyclerView.visibility=View.VISIBLE
            }
            if (editText.text.isBlank() &&  binding.cvSearchbar.visibility==View.GONE) {
                binding.appRecyclerView.visibility=View.GONE
            }
            editText.clearFocus()
            //binding.cvSearchbar.visibility=View.GONE
            //binding.editText.clearFocus()
            val scrcoords = IntArray(2)
            view.getLocationOnScreen(scrcoords)
            val x = ev.rawX + view.getLeft() - scrcoords[0]
            val y = ev.rawY + view.getTop() - scrcoords[1]
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom()) (this.getSystemService(
                INPUT_METHOD_SERVICE
            ) as InputMethodManager).hideSoftInputFromWindow(
                this.window.decorView.applicationWindowToken, 0
            )
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        binding.editText.setText("")
        binding.editText.clearFocus()
    }

    override fun onResume() {
        super.onResume()
        hiddenApps.clear()
        if (MySharedPreferences.getStringList(applicationContext).isNotEmpty()){
            hiddenApps = MySharedPreferences.getStringList(applicationContext) as ArrayList<String>
        }

        binding.editText.clearFocus()
        val data1=ArrayList<NotificationEntity>()
        data1.addAll(data)
        data.clear()
        notificationsAdapter.notifyDataSetChanged()
        data.addAll(data1)
        notificationsAdapter.notifyDataSetChanged()
        retrieveAndSetImage()

    }

    override fun onPause() {
        super.onPause()
        closeAppDrawer()
    }


    private fun retrieveAndSetImage() {
        val retrievedUserInfo = MySharedPreferences.getUserInfo(this)

        if (retrievedUserInfo != null) {
            Glide.with(this)
                .load(retrievedUserInfo.userImage)
                .placeholder(R.drawable.new_avatar) // Placeholder image resource
                .into(binding.ivThumbnail)
        }
    }

    private fun appDrawer() {
        binding.drawerLayout.setViewScale(
            GravityCompat.START,
            0.9f
        )
        binding.drawerLayout.setViewElevation(
            GravityCompat.START,
            30f
        )
        binding.drawerLayout.setViewScrimColor(
            GravityCompat.START,
            ContextCompat.getColor(this, R.color.white)
        )
        binding.drawerLayout.drawerElevation = 30f
        binding.drawerLayout.setRadius(GravityCompat.START, 25f)
        binding.drawerLayout.setViewRotation(GravityCompat.START, 0f)
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        binding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
            override fun onDrawerOpened(drawerView: View) {}
            override fun onDrawerClosed(drawerView: View) {}
            override fun onDrawerStateChanged(newState: Int) {}
        })
        initAppDrawer()
    }

    private var intervalList = mutableListOf<Int>()
    private val intervals = listOf(0, 1, 5, 10, 15, 30, 45, 60)
    private var currentIntervalIndex = 0
    @SuppressLint("SetTextI18n")
    private fun initAppDrawer() {
        binding.appMenuBtn.setOnClickListener {
            openAppDrawer()
        }

        binding.navMenuDrawer.bugBtn.setOnClickListener {
            startActivity(Intent(this, BugReportActivity::class.java))
            closeAppDrawer()
        }

        binding.navMenuDrawer.priorityModeSwitch.setOnClickListener {
            MySharedPreferences.savePriority(binding.navMenuDrawer.priorityModeSwitch.isChecked, this)
            refreshData()
        }

        val seekbarText = binding.navMenuDrawer.minutesTv

        val positiveButton = binding.navMenuDrawer.plus
        val negativeButton = binding.navMenuDrawer.minus

        positiveButton.setOnClickListener {
            if (currentIntervalIndex < intervals.size - 1) {
                currentIntervalIndex++
                updateIntervalList()
            }
        }

        negativeButton.setOnClickListener {
            if (currentIntervalIndex > 0) {
                currentIntervalIndex--
                updateIntervalList()
            }
            if (currentIntervalIndex == 0) {
                refreshData()
            }
        }


        val savedInterval = MySharedPreferences.getNotificationInterval(this)
        currentIntervalIndex = intervals.indexOf(savedInterval)

        if (savedInterval == 0) {
            seekbarText.text = "0 Minutes"

        } else {
            seekbarText.text = "$savedInterval Minutes"
        }
    }

    private fun updateIntervalList() {
        val currentInterval = intervals[currentIntervalIndex]
        intervalList.add(currentInterval)
        MySharedPreferences.saveNotificationsInterval(this, currentInterval)
        binding.navMenuDrawer.minutesTv.text = "$currentInterval Minutes"
        Log.d("IntervalList", intervalList.toString())
        // Update UI or perform additional actions with the intervals
    }

    private fun closeAppDrawer() {
        binding.drawerLayout.closeDrawer(
            GravityCompat.START
        )
    }

    private fun openAppDrawer() {
        binding.drawerLayout.openDrawer(
            GravityCompat.START
        )
    }

    private fun lightDarkMode() {
        val dayNightSwitch = binding.navMenuDrawer.dayNightSwitch
        dayNightSwitch.isChecked = ThemeSettings.getInstance(this)!!.nightMode
        dayNightSwitch.setOnClickListener {
            Log.e("darkMode", ThemeSettings.getInstance(this)!!.nightMode.toString())
            if (dayNightSwitch.isChecked) {
                binding.drawerLayout.closeDrawer(
                    GravityCompat.START
                )
                Handler().postDelayed({
                    ThemeSettings.getInstance(this@MainActivity)?.nightMode = true
                    ThemeSettings.getInstance(this@MainActivity)?.refreshTheme(this)
                    val intent = Intent(this@MainActivity, this@MainActivity.javaClass)
                    intent.putExtras(getIntent())
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    finish()
                    startActivity(intent)
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }, 500)

            } else {
                binding.drawerLayout.closeDrawer(
                    GravityCompat.START
                )
                Handler().postDelayed({
                    ThemeSettings.getInstance(this@MainActivity)?.nightMode = false
                    ThemeSettings.getInstance(this@MainActivity)?.refreshTheme(this)
                    val intent = Intent(this@MainActivity, this@MainActivity.javaClass)
                    intent.putExtras(getIntent())
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    finish()
                    startActivity(intent)
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }, 500)

            }
        }

    }

    private fun isNotificationServiceEnabled(): Boolean {
        val cn = ComponentName(this, NotificationListener::class.java)
        val flat = Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
        return flat?.contains(cn.flattenToString()) ?: false
    }

    private fun getNotiData(sbn: StatusBarNotification): NotificationsModel {
        val notification = sbn.notification

        val title = notification.extras.getString(Notification.EXTRA_TITLE)
        val text = notification.extras.getString(Notification.EXTRA_TEXT)

        val notificationTime = sbn.postTime // Notification timestamp in milliseconds

        // Get the notification icon (small icon)
        val smallIconBitmap: Bitmap? = sbn.notification.smallIcon?.loadDrawable(this)?.toBitmap()
        val smallIconByteArray = smallIconBitmap?.let { bitmapToByteArray(it) }

        // You can also get the large icon (if available)
        val packageName = sbn.packageName
        val appInfo: AppInfo? = getApplicationInfo(this, packageName)
        val appIcon: Drawable = byteArrayToDrawable(appInfo!!.appIconByteArray)
        val iconAsString = Base64.encodeToString(appInfo.appIconByteArray, Base64.DEFAULT)

        // Send all notification details via broadcast
        val intent = Intent("ACTION_NOTIFICATION_RECEIVED")
        intent.putExtra("title", title)
        intent.putExtra("text", text)
        intent.putExtra("pkg", packageName)
        intent.putExtra("app_info", appInfo)
        intent.putExtra("heads-up", isHeadsUpNotification(notification))
        intent.putExtra("notificationTime", notificationTime)
        intent.putExtra("smallIcon", smallIconByteArray)
        intent.putExtra("notiId", sbn.key)

        return NotificationsModel(
            formatTimeFromTimestamp(notificationTime),
            "",
            title,
            text,
            smallIconBitmap!!,
            "simple",
            "", packageName, appInfo.appName, iconAsString, sbn.key
        )
    }

    fun formatTimeFromTimestamp(timestamp: Long): String {
        val pattern = "h:mm a"
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        return sdf.format(timestamp)
    }

    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    fun byteArrayToDrawable(byteArray: ByteArray): Drawable {
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        return BitmapDrawable(resources, bitmap)
    }

    fun drawableToByteArray(drawable: Drawable): ByteArray {
        val bitmap = (drawable).toBitmap()
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    fun isHeadsUpNotification(notification: Notification): Boolean {
        // Check if the priority is set to PRIORITY_HIGH or PRIORITY_MAX
        return (notification.priority == NotificationCompat.PRIORITY_HIGH ||
                notification.priority == NotificationCompat.PRIORITY_MAX)
    }

    fun getApplicationInfo(context: Context, packageName: String): AppInfo? {
        val packageManager: PackageManager = context.packageManager

        return try {
            val appInfo: ApplicationInfo = packageManager.getApplicationInfo(packageName, 0)
            val appName: String = packageManager.getApplicationLabel(appInfo).toString()
            val appIcon: Drawable = packageManager.getApplicationIcon(appInfo)
            val appIconByteArray = drawableToByteArray(appIcon)
            AppInfo(appName, appIconByteArray)
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
    }
}

