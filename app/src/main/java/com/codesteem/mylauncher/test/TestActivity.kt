package com.codesteem.mylauncher.test

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.codesteem.mylauncher.R
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codesteem.mylauncher.models.AppInfo
import com.codesteem.mylauncher.util.MySharedPreferences
import com.codesteem.mylauncher.util.MySharedPreferences.getStringList
import com.codesteem.mylauncher.gesture.DefaultItemClickListener
import com.codesteem.mylauncher.gesture.GestureAdapter
import com.codesteem.mylauncher.gesture.GestureManager
import com.codesteem.mylauncher.gesture.RecyclerItemTouchListener

class TestActivity : AppCompatActivity() {
    private var hiddenIndex: Int=-1
    private var hiddenAdded: Boolean=false
    private lateinit var adapterApps: MonthsAdapter
    private var installedAppsList: List<MonthItem> =mutableListOf<MonthItem>()
    private lateinit var recyclerView: RecyclerView
    protected var gestureManager: GestureManager? = null

    //    protected open val months: MutableList<MonthItem>
//        get() {
//            return mutableListOf(
//                Month("JAN", R.drawable.new_avatar),
//                Month("FEB", R.drawable.new_avatar),
//                Month("MAR", R.drawable.new_avatar),
//                MonthHeader("Hidden Apps"),
//                Month("APR", R.drawable.new_avatar),
//                Month("MAY", R.drawable.new_avatar),
//                Month("JUN", R.drawable.new_avatar))
//        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        recyclerView = findViewById(R.id.recyclerView)
        val intentFilter = IntentFilter("ACTION_NOTIFICATION_RECEIVED")
        registerReceiver(notificationReceiver, intentFilter)
        val manager = GridLayoutManager(applicationContext, 4)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = manager
        installedAppsList = getAllInstalledApps()
        val newList = getStringList(applicationContext) // Replace with your actual way of getting newList

        val filteredApps = filterInstalledApps(installedAppsList, newList)
        installedAppsList=filteredApps
        //adapterApps = MonthsAdapter(R.layout.grid_item)
        adapterApps.data = installedAppsList.toMutableList()
        recyclerView.adapter = adapterApps
        recyclerView.addOnItemTouchListener(RecyclerItemTouchListener(object :
            DefaultItemClickListener<MonthItem>() {

            override fun onItemClick(item: MonthItem, position: Int): Boolean {
//                Snackbar.make(
//                    recyclerView,
//                    "Click event on the $position position",
//                    Snackbar.LENGTH_SHORT
//                ).show()
                val model=installedAppsList.get(position) as Month
                launchApp(model.packageName)
                return true
            }

            override fun onItemLongPress(item: MonthItem, position: Int) {
                if (!hiddenAdded){
                    adapterApps.add(MonthHeader("Hidden Apps"))
                    hiddenIndex=adapterApps.itemCount-1
                    for (i in newList){
                        val packageManager = packageManager
                        val appInfo = packageManager.getApplicationInfo(i, 0)
                        val appLogo = packageManager.getApplicationIcon(appInfo)
                        adapterApps.add(Month(appInfo.packageName, appLogo, appInfo.packageName, openCounter = 0))
                    }

                }
                hiddenAdded=true

            }

            override fun onDoubleTap(item: MonthItem, position: Int): Boolean {
//                Snackbar.make(
//                    recyclerView,
//                    "Double tap event on the $position position",
//                    Snackbar.LENGTH_SHORT
//                ).show()
                return true
            }
        }))

        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val item = adapterApps.getItem(position)
                return if (item.type === MonthItem.MonthItemType.HEADER) manager.spanCount else 1
            }
        }

        gestureManager = GestureManager.Builder(recyclerView)
            .setSwipeEnabled(true)
            .setLongPressDragEnabled(true)
            .build()

        adapterApps.setDataChangeListener(object : GestureAdapter.OnDataChangeListener<MonthItem> {
            override fun onItemRemoved(item: MonthItem, position: Int, direction: Int) {
//                Snackbar.make(
//                    recyclerView,
//                    "Month removed from position $position",
//                    Snackbar.LENGTH_SHORT
//                ).show()
            }

            override fun onItemReorder(item: MonthItem, fromPos: Int, toPos: Int) {
//                Snackbar.make(
//                    recyclerView,
//                    "Month moved from position $fromPos to $toPos",
//                    Snackbar.LENGTH_SHORT
//                ).show()

                hiddenIndex=findMonthHeaderIndexByName("Hidden Apps",adapterApps.data)
                if (hiddenIndex!=-1){
                    var newList: MutableList<String> = ArrayList()
                    if (getStringList(applicationContext).isNotEmpty() &&getStringList(applicationContext)!=null){

                        newList= getStringList(applicationContext).toMutableList()
                    }
                    val singleItem=item as Month
                    if (toPos>hiddenIndex){
                        newList.add(singleItem.packageName)
                        Log.e("newList",newList.size.toString())
                        MySharedPreferences.saveStringList(applicationContext, newList)
                    }else{
                        newList.remove(singleItem.packageName)
                        Log.e("newList",newList.size.toString())
                        MySharedPreferences.saveStringList(applicationContext, newList)
                    }
                }
            }
        })
    }
    private fun launchApp(packageName: String) {
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        intent?.let {
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
    private fun getAllInstalledApps(): List<MonthItem> {
        val packageManager = packageManager
        val installedApps = mutableListOf<MonthItem>()

        val apps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        for (appInfo in apps) {
            if (packageManager.getLaunchIntentForPackage(appInfo.packageName) != null) {
                val appLogo = packageManager.getApplicationIcon(appInfo)
                installedApps.add(Month(appInfo.packageName, appLogo, appInfo.packageName, openCounter = 0))
            }

        }
        return installedApps
    }
    fun filterInstalledApps(installedApps: List<MonthItem>, newList: List<String>): List<MonthItem> {
        return installedApps.filter { item ->
            item is Month && item.packageName !in newList
        }
    }

    private val notificationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "ACTION_NOTIFICATION_RECEIVED") {
                val title = intent.getStringExtra("title")
                val text = intent.getStringExtra("text")
                val pkg = intent.getStringExtra("pkg")
                val receivedAppInfo: AppInfo? = intent.getParcelableExtra("app_info")

                val appName: String = receivedAppInfo!!.appName
                /*if (pkg?.let { packageManager.getLaunchIntentForPackage(it) } != null){
                    val index=findIndexByPackageName(pkg)
                    val model=adapterApps.getItem(index) as Month
                    var counter=0
                    counter = if (model.counterNotification==null){
                        1
                    }else{
                        model.counterNotification.plus(1)
                    }


                    adapterApps.remove(index)
                    adapterApps.insert(Month(model.packageName, model.drawableId, model.packageName,
                        counter
                    ), index)

                }*/

                // Process the notification data received from the broadcast
                // For example, display the notification data in the UI
            }
        }
    }
    fun findIndexByPackageName(packageNameToFind: String): Int {
        for ((index, item) in installedAppsList.withIndex()) {
            if (item is Month && item.packageName == packageNameToFind) {
                return index
            }
        }
        return -1 // Return -1 if the package name is not found in the list
    }


}