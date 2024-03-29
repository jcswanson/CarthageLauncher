package com.codesteem.mylauncher

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import android.view.DragEvent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.codesteem.mylauncher.adapter.AppSearchAdapter
import com.codesteem.mylauncher.databinding.ActivityMenuBinding
import com.codesteem.mylauncher.databinding.DialogLayoutBinding
import com.codesteem.mylauncher.databinding.NotificationLayoutBinding
import com.codesteem.mylauncher.gesture.DefaultItemClickListener
import com.codesteem.mylauncher.gesture.GestureAdapter
import com.codesteem.mylauncher.gesture.GestureManager
import com.codesteem.mylauncher.gesture.RecyclerItemTouchListener
import com.codesteem.mylauncher.main.MainViewModel
import com.codesteem.mylauncher.models.AppInfo
import com.codesteem.mylauncher.models.AppSearchModel
import com.codesteem.mylauncher.models.NotificationsModel
import com.codesteem.mylauncher.test.Month
import com.codesteem.mylauncher.test.MonthHeader
import com.codesteem.mylauncher.test.MonthItem
import com.codesteem.mylauncher.test.MonthsAdapter
import com.codesteem.mylauncher.util.MySharedPreferences
import com.codesteem.mylauncher.util.MySharedPreferences.getPrioritized
import com.codesteem.mylauncher.util.MySharedPreferences.getStringList
import com.codesteem.mylauncher.util.MySharedPreferences.getStringListAll
import com.codesteem.mylauncher.util.MySharedPreferences.isAppsBallonShown
import com.codesteem.mylauncher.util.MySharedPreferences.isPrioritized
import com.codesteem.mylauncher.util.MySharedPreferences.modifyPair
import com.codesteem.mylauncher.util.MySharedPreferences.saveAppsShownBallon
import com.codesteem.mylauncher.util.NotificationListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.showAlignBottom
import com.skydoves.powermenu.OnMenuItemClickListener
import com.skydoves.powermenu.PowerMenu
import com.skydoves.powermenu.PowerMenuItem
import com.tapadoo.alerter.Alerter
import dagger.hilt.android.AndroidEntryPoint
import io.github.g00fy2.quickie.ScanQRCode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class MenuActivity : AppCompatActivity(), DecoratedBarcodeView.TorchListener{
    private lateinit var viewModel: MainViewModel
    private lateinit var barcodeScannerView: DecoratedBarcodeView

    private var data: ArrayList<NotificationsModel> = ArrayList()

    private lateinit var cvSearchbar: CardView
    private var editText: EditText? = null
    private lateinit var binding: ActivityMenuBinding

    private var hiddenIndex: Int=-1
    private var hiddenAdded: Boolean=false
    private lateinit var appsAdapter2: MonthsAdapter
    private var installedAppsList: List<MonthItem> =mutableListOf<MonthItem>()
    protected var gestureManager: GestureManager? = null
    private var appSearchModelsList: java.util.ArrayList<AppSearchModel> = ArrayList()
    private lateinit var dialogAdapter: AppSearchAdapter
    private lateinit var telephonyManager: TelephonyManager

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private var currentUninstall = ""
    private var powerMenu: PowerMenu? = null
    private var currentPopupItem: MonthItem? = null

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

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "appsScreen")
        }
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_SCREEN_ON)
            addAction(Intent.ACTION_SCREEN_OFF)
        }
        registerReceiver(screenStateChangedReceiver, filter)

        barcodeScannerView = binding.qrCodeView
        barcodeScannerView.statusView.text = "Scan QR code"

        LocalBroadcastManager.getInstance(this).registerReceiver(
            mMessageReceiver, IntentFilter("NOTI"))

        telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            enableFullScreenMode()
        }
        hideSystemUI()
        fiveTapsBallon()
        setupAppsRv()
        refreshData()
        refreshNotificationCounters()
        val btnSearch = binding.btnSearch
        cvSearchbar = binding.cvSearchbar
        editText = binding.editText

        initCode()
        findViewById<ImageView>(R.id.back).setOnClickListener {
            editText?.setText("")
            editText?.clearFocus()
            val newList: MutableList<String> = ArrayList()

            for (i in appsAdapter2.data){
                if (i.type==MonthItem.MonthItemType.HEADER){
                    break
                }
            }
            if (binding.qrCodeView.visibility == View.VISIBLE) {
                binding.qrCodeView.visibility = View.GONE
                barcodeScannerView.pause()
                binding.bottomNavigation.visibility = View.VISIBLE
            } else {
                finish()
            }

        }

        btnSearch.setOnClickListener {
            cvSearchbar.visibility=View.VISIBLE
            editText?.requestFocus()
            showKeyboard(view = editText!!)

        }

        barcodeScannerView.decodeContinuous { result ->
            Log.e("result", result.toString())
            result?.result?.text?.let { s1 ->
                showDialog(this@MenuActivity, s1)
            }

            barcodeScannerView.pause()
            binding.qrCodeView.visibility = View.GONE
        }
        barcodeScannerView.pause()
        binding.frameLayout.setOnClickListener {
            binding.qrCodeView.visibility = View.VISIBLE
            barcodeScannerView.resume()
            binding.bottomNavigation.visibility = View.GONE
        }
        binding.applicationsTv.setOnClickListener {
            if (binding.qrCodeView.visibility == View.VISIBLE) {
                binding.qrCodeView.visibility = View.GONE
                barcodeScannerView.pause()
                binding.bottomNavigation.visibility = View.VISIBLE
            }
        }
        binding.applicationsTv.setOnLongClickListener {
            if (!hiddenAdded) {
                appsAdapter2.add(MonthHeader("Hidden Apps"))
                hiddenIndex = appsAdapter2.itemCount - 1
                val hiddenList = getStringList(applicationContext)
                for (i in hiddenList) {
                    val packageManager = packageManager
                    val appPackageName = getPackageNameByName(this, i)
                    appPackageName?.let { pack ->
                        val appInfo = packageManager.getApplicationInfo(pack, 0)
                        val appLogo = packageManager.getApplicationIcon(appInfo)
                        appsAdapter2.add(Month(i, appLogo, appInfo.packageName, openCounter = 0))
                    }

                }
                binding.appsRv.scrollToPosition(appsAdapter2.data.lastIndex - 1)
                appsAdapter2.notifyDataSetChanged()
                hiddenAdded = true
            } else {
                hiddenAdded = false
                hiddenIndex = findMonthHeaderIndexByName("Hidden Apps", appsAdapter2.data)
                val hiddenList = getStringList(applicationContext)
                if (hiddenIndex >= 0 && hiddenIndex < appsAdapter2.data.size) {
                    appsAdapter2.data.removeAt(hiddenIndex)
                }
                appsAdapter2.data.removeAll { hiddenList.contains(it.name) }
                appsAdapter2.notifyDataSetChanged()
            }
            return@setOnLongClickListener true
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

    override fun onTorchOn() {
    }

    override fun onTorchOff() {
    }

    private fun fiveTapsBallon() {
        val balloon = Balloon.Builder(this)
            .setWidthRatio(1.0f)
            .setHeight(BalloonSizeSpec.WRAP)
            .setText("Press and hold to reveal/hide hidden apps")
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
            saveAppsShownBallon(this)
        }
        if (!isAppsBallonShown(this)) {
            binding.applicationsTv.showAlignBottom(balloon)
        }
    }

    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val message = intent.getBooleanExtra("Status", false)
            refreshNotificationCounters()
            Log.e("message", message.toString())
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        editText?.setText("")
        editText?.clearFocus()

        for (i in appsAdapter2.data){
            if (i.type==MonthItem.MonthItemType.HEADER){
                break
            }
        }

        if (binding.qrCodeView.visibility == View.VISIBLE) {
            binding.qrCodeView.visibility = View.GONE
        } else {
            finish()
        }
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

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val view = currentFocus
        if (view != null && (ev.action == MotionEvent.ACTION_UP || ev.action == MotionEvent.ACTION_MOVE) && view is EditText && !view.javaClass.name.startsWith(
                "android.webkit."
            )
        ) {
            if (editText?.text?.isNotBlank() == true) {
                binding.appsRv.visibility=View.GONE
            } else {
                cvSearchbar.visibility=View.GONE
                binding.appsRv.visibility=View.VISIBLE
            }
            editText?.clearFocus()
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

    private fun showKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun setupAppsRv() {
        appsAdapter2 = MonthsAdapter(R.layout.grid_item)
        val manager = GridLayoutManager(applicationContext, 4)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val item = appsAdapter2.getItem(position)
                return if (item.type === MonthItem.MonthItemType.HEADER) manager.spanCount else 1
            }
        }

        binding.appsRv.itemAnimator = null
        binding.appsRv.layoutManager = manager
        binding.appsRv.setHasFixedSize(true)
        binding.appsRv.adapter = appsAdapter2
        binding.appsRv.addOnItemTouchListener(RecyclerItemTouchListener(object : DefaultItemClickListener<MonthItem>() {
            override fun onItemClick(item: MonthItem, position: Int): Boolean {
                Log.e("click", "click")
                val model = installedAppsList.getOrNull(position) as? Month
                var hideAppSelected: Month? = null
                (appsAdapter2.data as? List<MonthItem>)?.forEach { hiddenItem ->
                    if (item is Month && hiddenItem is Month && hiddenItem.packageName == item.packageName) {
                        hideAppSelected = hiddenItem
                    }
                }
                val appSelected = model ?: hideAppSelected
                appSelected?.packageName?.let { app ->
                    launchApp(app)
                }

                return true
            }

            override fun onItemLongPress(item: MonthItem, position: Int) {
                hiddenIndex=findMonthHeaderIndexByName("Hidden Apps",appsAdapter2.data)
                if (hiddenIndex != -1 && position > hiddenIndex) {
                    manager.findViewByPosition(position)?.let { setupHiddenPopup(item, it) }
                } else {
                    manager.findViewByPosition(position)?.let { setupPopup(item, position, it) }
                }

                super.onItemLongPress(item, position)
            }
        }))

        gestureManager = GestureManager.Builder(binding.appsRv)
            .setSwipeEnabled(false)
            .setLongPressDragEnabled(false)
            .setManualDragEnabled(false)
            .build()


        appsAdapter2.setDataChangeListener(object : GestureAdapter.OnDataChangeListener<MonthItem> {
            override fun onItemRemoved(item: MonthItem, position: Int, direction: Int) {
                Log.e("removed", "removed")
                hiddenIndex=findMonthHeaderIndexByName("Hidden Apps",appsAdapter2.data)
                Log.e("name", item.name)

                uninstallApp2(item.name)
                if (position<hiddenIndex){
                    Snackbar.make(
                        binding.appsRv,
                        "App is deleted.",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onItemReorder(item: MonthItem, fromPos: Int, toPos: Int) {
                installedAppsList.indexOf(item).let {
                    Log.e("reorder", "${item.name} = position: $it, from: $fromPos, to: $toPos")
                }
            }
        })
    }

    private val menuClickListener =
        OnMenuItemClickListener<PowerMenuItem> { position, choice ->

            if (choice.title == "Hide") {
                var newList: MutableList<String> = ArrayList()
                newList= getStringList(applicationContext).toMutableList()
                Log.e("newList", newList.toString())
                currentPopupItem?.name?.let { newList.add(it) }
                Log.e("currentPopup", currentPopupItem?.name.toString())
                MySharedPreferences.saveStringList(applicationContext, newList)
                powerMenu?.dismiss()
                refreshData()
            } else if (choice.title == "Un-hide") {
                var newList: MutableList<String> = ArrayList()
                newList= getStringList(applicationContext).toMutableList()
                newList.remove(currentPopupItem?.name).also {
                    Log.e("remove", it.toString())
                }

                MySharedPreferences.saveStringList(applicationContext, newList)
                hiddenAdded = false
                hiddenIndex=findMonthHeaderIndexByName("Hidden Apps",appsAdapter2.data)
                val hiddenList = getStringList(applicationContext)
                appsAdapter2.data.removeAt(hiddenIndex)
                appsAdapter2.data.removeAll { hiddenList.contains(it.name) }
                appsAdapter2.notifyDataSetChanged()
                powerMenu?.dismiss()
                refreshData()
            } else if (choice.title == "Prioritize") {
                currentPopupItem?.name?.let {
                    val packageName = getPackageNameByName(this, it)
                    packageName?.let { packName ->
                        MySharedPreferences.savePriority(packName, this)
                    }
                    powerMenu?.dismiss()
                }

            } else if (choice.title == "Un-prioritize") {
                currentPopupItem?.name?.let {
                    val packageName = getPackageNameByName(this, it)
                    packageName?.let {
                        MySharedPreferences.removePriority(it, this)
                    }
                    powerMenu?.dismiss()
                }
            } else {
                powerMenu?.dismiss()
                currentPopupItem?.name?.let { uninstallApp2(it) }
            }
        }

    private fun setupPopup(item: MonthItem, ItemPosition: Int, view: View) {
        currentPopupItem = item
        val appPackage = getPackageNameByName(this, item.name)

        powerMenu = PowerMenu.Builder(this)
            .addItem(PowerMenuItem(if (isPrioritized(appPackage)) "Un-prioritize" else "Prioritize"))
            .addItem(PowerMenuItem("Hide"))
            .addItem(PowerMenuItem("Uninstall"))
            .setTextGravity(Gravity.CENTER)
            .setOnMenuItemClickListener(menuClickListener)
            .build()
        powerMenu?.showAsAnchorCenter(view)
    }

    private fun isPrioritized(app: String?): Boolean {
        return isPrioritized(app.toString(), this)
    }

    private fun setupHiddenPopup(item: MonthItem, view: View) {
        currentPopupItem = item
        val appPackage = getPackageNameByName(this, item.name)
        powerMenu = PowerMenu.Builder(this)
            .addItem(PowerMenuItem(if (isPrioritized(appPackage)) "Un-prioritize" else "Prioritize"))
            .addItem(PowerMenuItem("Un-hide"))
            .addItem(PowerMenuItem("Uninstall"))
            .setTextGravity(Gravity.CENTER)
            .setOnMenuItemClickListener(menuClickListener)
            .build()
        powerMenu?.showAsAnchorCenter(view)
    }


    private fun refreshNotificationCounters() {
        if (isNotificationServiceEnabled()) {
            NotificationListener.instance?.getNotifications()?.forEach {
                val index = findIndexByPackageName(it.packageName)
                if (index != -1) {
                    val model = appsAdapter2.getItem(index) as Month
                    model.counterNotification = it.notification.number
                    appsAdapter2.notifyItemChanged(index)
                    Log.e("counter", "${model.packageName}:${model.counterNotification}")
                }
            }
        }
    }
    private fun refreshData() {
        installedAppsList = getAllInstalledAppsItems()
        val newList = getStringList(applicationContext)
        val filteredApps = filterInstalledApps(installedAppsList, newList)
        installedAppsList = filteredApps
        appsAdapter2.data = installedAppsList.toMutableList()
        binding.appsRv.visibility = View.VISIBLE
        appsAdapter2.notifyDataSetChanged()
    }

    private fun initCode(){
        val intentFilter = IntentFilter("ACTION_NOTIFICATION_RECEIVED")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(notificationReceiver, intentFilter, RECEIVER_EXPORTED)
        } else {
            registerReceiver(notificationReceiver, intentFilter)
        }

        initSearchBarData()
    }

    override fun onResume() {
        super.onResume()
        editText?.clearFocus()

        // Register the receiver
        val filter5 = IntentFilter(Intent.ACTION_PACKAGE_ADDED)
        filter5.addDataScheme("package")
        registerReceiver(appInstallReceiver, filter5)
    }

    private val registerForUninstall = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        lifecycleScope.launch {
            delay(3000)
            var newList: MutableList<Pair<String, Int>> = ArrayList()
            newList = getStringListAll(applicationContext).toMutableList()
            newList.find {
                it.first == getPackageNameByName(this@MenuActivity, currentUninstall)
            }?.let {
                newList.remove(it)
                MySharedPreferences.saveStringListAll(applicationContext, newList)
            }
            refreshData()
            isPackageInstalled(currentUninstall).also { Log.e("installed", it.toString()) }
        }
    }

    private fun Context.isPackageInstalled(packageName: String): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    fun uninstallApp2(appName: String){
        val packageName = getPackageNameByName(this, appName)
        val uri: Uri = Uri.fromParts("package", packageName, null)
        val uninstallIntent = Intent(Intent.ACTION_DELETE, uri)
        currentUninstall = appName
        registerForUninstall.launch(uninstallIntent)
    }

    private val scanQrCodeLauncher = registerForActivityResult(ScanQRCode()) { result ->
        // handle QRResult
        if (result.toString().contains("QRSuccess")){
            val rawValue = extractRawValueFromString(result.toString())
            val s1  = rawValue
            showDialog(this, s1.toString())
        }
    }

    private fun extractRawValueFromString(inputString: String): String? {
        val regex = "rawValue=(.*?)\\,"
        val matchResult = regex.toRegex().find(inputString)
        return matchResult?.groupValues?.getOrNull(1)
    }

    private fun showDialog(context: Context, text: String) {
        val binding = DialogLayoutBinding.inflate(LayoutInflater.from(context))
        val dialogView = binding.root

        binding.textView.text = text
        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()
        if (text.isValidUrl()) {
            binding.openLinkButton.isVisible = true
            binding.openLinkButton.setOnClickListener {
                dialog.dismiss()
                try {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(text))
                    context.startActivity(browserIntent)
                } catch (_: Exception) {

                }
            }
        } else {
            binding.openLinkButton.isVisible = false
        }
        dialog.show()
    }

    private fun String.isValidUrl(): Boolean {
        return android.util.Patterns.WEB_URL.matcher(this).matches()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(notificationReceiver)
        unregisterReceiver(appInstallReceiver)
        unregisterReceiver(screenStateChangedReceiver)
    }

    private fun launchApp(packageName: String) {
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        intent?.let {
            modifyPair(this, packageName)
            startActivity(it)
        } ?: run {
            // If the intent is null, the app is not launchable.
            Toast.makeText(applicationContext, "App not launchable", Toast.LENGTH_SHORT).show()
        }
    }

    fun findMonthHeaderIndexByName(headerName: String, data: MutableList<MonthItem>): Int {
        for ((index, item) in data.withIndex()) {
            if (item is MonthHeader && item.name == headerName) {
                return index
            }
        }
        return -1 // Return -1 if a MonthHeader with the given name is not found in the list
    }

    private fun getAllInstalledAppsItems(): List<MonthItem> {
        val packageManager = packageManager
        val installedApps = mutableListOf<MonthItem>()
        var newList: MutableList<Pair<String, Int>> = ArrayList()
        if (getStringListAll(applicationContext).isNotEmpty()){
            newList= getStringListAll(applicationContext).toMutableList()
        }
        val apps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        for (i in apps.indices) {
            val appInfo= apps[i]
            if (packageManager.getLaunchIntentForPackage(appInfo.packageName) != null) {
                val appLogo = packageManager.getApplicationIcon(appInfo)
                val appTitle = packageManager!!.getApplicationLabel(appInfo).toString()
                val packageName = appInfo.packageName
                appSearchModelsList.add(AppSearchModel(appTitle,appLogo,packageName))
                if (getStringListAll(applicationContext).isEmpty()){
                    newList.add(Pair(packageName, 0))
                } else{

                    if (newList.find { it.first.contains(packageName) } == null) newList.add(Pair(packageName, 0))
                }
            }
            if (i==apps.size-1){
                if (getStringListAll(applicationContext).isEmpty()) {
                    MySharedPreferences.saveStringListAll(applicationContext, newList)
                }
            }
        }

        newList.forEach { i ->
            getAppInfo(i.first)?.let { appInfo ->
                if (appInfo.packageName != packageName) {
                    val appLogo = packageManager.getApplicationIcon(appInfo)
                    val appName = appInfo.loadLabel(packageManager).toString()
                    installedApps.add(
                        Month(
                            appName,
                            appLogo,
                            appInfo.packageName,
                            openCounter = i.second
                        )
                    )
                }
            }
        }

        return installedApps.sortedWith(compareByDescending<MonthItem> { it.openCounter }.thenBy {it.name})
    }

    private fun filterInstalledApps(installedApps: List<MonthItem>, newList: List<String>): List<MonthItem> {
        Log.e("filter", newList.toString())
        return installedApps.filter { item ->
            item is Month && item.name !in newList
        }
    }

    private val notificationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "ACTION_NOTIFICATION_RECEIVED") {
                val id = intent.getStringExtra("id")
                val title = intent.getStringExtra("title")
                val text = intent.getStringExtra("text")
                val pkg = intent.getStringExtra("pkg")
                val notificationTime = intent.getLongExtra("notificationTime",0L)
                val headsUp = intent.getBooleanExtra("heads-up",false)
                val receivedAppInfo: AppInfo? = intent.getParcelableExtra("app_info")
                val smallIconByteArray = intent.getByteArrayExtra("smallIcon")
                val isReply = intent.getBooleanExtra("replyAction", false)
                val msgId = intent.getStringExtra(NotificationListener.MSG_ID)
                val isActivityInForeground = this@MenuActivity.lifecycle.currentState.isAtLeast(
                    Lifecycle.State.RESUMED
                )
                val actions = intent.getParcelableArrayExtra("actions", Notification.Action::class.java)
                if (canShowNotification() && isActivityInForeground) {

                    showNotificationAlert(id,this@MenuActivity, title, text, smallIconByteArray, isReply, actions!!, msgId)
                }
                val appName: String = receivedAppInfo!!.appName
                if (pkg?.let { packageManager.getLaunchIntentForPackage(it) } != null){
                    try{
                        if (!data.containsNotification(
                                formatTimeFromTimestamp(notificationTime),
                                text
                            ) && !text!!.contains("new messages")
                        ) {
                            data.add(
                                0,
                                NotificationsModel(
                                    formatTimeFromTimestamp(notificationTime),
                                    "",
                                    title,
                                    text,
                                    null,
                                    "simple",
                                    "", pkg, appName, null
                                )
                            )
                            val index=findIndexByPackageName(pkg)
                            val model= appsAdapter2.getItem(index) as Month
                            val counter=model.counterNotification!!.plus(1)
                            appsAdapter2.remove(index)
                            appsAdapter2.insert(Month(model.packageName, model.drawableId, model.packageName,
                                counter, model.openCounter
                            ), index)
                            appsAdapter2.notifyDataSetChanged()
                        }
                    }catch (ex:Exception){
                        Log.e("counter",ex.message.toString())

                    }
                }
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

    @SuppressLint("ClickableViewAccessibility")
    private fun showNotificationAlert(
        id: String?,
        activity: Activity,
        title: String?,
        content: String?,
        icon: ByteArray?,
        isReply: Boolean,
        actions: Array<Notification.Action>,
        msgID: String?
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
                                        id?.let { viewModel.deleteNotification(it) }
                                        NotificationListener.instance?.reply(msgID, text.toString())
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
                                    id?.let { viewModel.deleteNotification(it) }
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


    fun List<NotificationsModel>.containsNotification(time: String?, desc: String?): Boolean {
        return any { it.time == time && it.desc == desc }
    }

    fun findIndexByPackageName(packageNameToFind: String): Int {
        for ((index, item) in installedAppsList.withIndex()) {
            if (item is Month && item.packageName == packageNameToFind) {
                return index
            }
        }
        return -1 // Return -1 if the package name is not found in the list
    }

    private fun initSearchBarData() {
        binding.searchRv.layoutManager = LinearLayoutManager(this)
        dialogAdapter = AppSearchAdapter(this@MenuActivity, binding.cvSearchbar,editText!!)
        binding.searchRv.adapter = dialogAdapter
        dialogAdapter.notifyDataSetChanged()

        editText?.doAfterTextChanged { text ->
            if (text?.isBlank() == true){
                binding.searchRv.visibility=View.GONE
                //cvSearchbar.visibility = View.GONE
                binding.appsRv.visibility=View.VISIBLE
            }else{
                binding.searchRv.visibility=View.VISIBLE
                binding.appsRv.visibility=View.GONE
                val searchText = text.toString()
                updateMatchingApps(searchText)
            }
        }

    }

    private fun updateMatchingApps(searchText: String) {
        val filteredApps = appSearchModelsList.filter {
            val trimmedTitle = it.title?.trim()
            trimmedTitle?.lowercase(Locale.ROOT)?.startsWith(searchText.trim().lowercase(Locale.ROOT)) ?: false
        }
        dialogAdapter.setApps(filteredApps)
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

        // App not found
        return null
    }

    private fun getAppInfo(packageName: String): ApplicationInfo? {
        return try {
            packageManager.getApplicationInfo(packageName, 0)
        } catch (_: Exception) {
            null
        }

    }

    private val appInstallReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == Intent.ACTION_PACKAGE_ADDED) {
                val packageName = intent.data?.schemeSpecificPart
                packageName?.let { newPackage ->
                    getAppInfo(newPackage)?.let { appInfo ->
                        val appLogo = packageManager.getApplicationIcon(appInfo)
                        val appName = appInfo.loadLabel(packageManager).toString()
                        val position = installedAppsList.size
                        val newApp = Month(appName, appLogo, appInfo.packageName, openCounter = 0)
                        installedAppsList.toMutableList().add(position, newApp)
                        var newList: MutableList<Pair<String, Int>> = ArrayList()
                        newList = getStringListAll(applicationContext).toMutableList()
                        Log.e("newList", newList.toString())
                        newList.add(position, Pair(appInfo.packageName, 0))
                        MySharedPreferences.saveStringListAll(applicationContext, newList)
                        appsAdapter2.data.add(position, newApp)
                        appsAdapter2.notifyItemInserted(position)
                    }
                }

                Log.e("AppInstallReceiver", "Installed: $packageName")
                // Handle the new app installation here
            }
        }
    }


    fun isNotificationServiceEnabled(): Boolean {
        val cn = ComponentName(this, NotificationListener::class.java)
        val flat = Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
        return flat?.contains(cn.flattenToString()) ?: false
    }

    fun formatTimeFromTimestamp(timestamp: Long): String {
        val pattern = "h:mm a"
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        return sdf.format(timestamp)
    }

    fun drawableToByteArray(drawable: Drawable): ByteArray {
        val bitmap = (drawable).toBitmap()
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }



}