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

/**
 * This activity displays a grid of apps installed on the device.
 * Users can interact with the apps by clicking, long-pressing, or double-tapping.
 */
class TestActivity : AppCompatActivity() {
    // Index of the hidden apps header in the adapterApps list
    private var hiddenIndex: Int = -1
    // Flag to track whether hidden apps have been added to the list
    private var hiddenAdded: Boolean = false

    // Adapter for the RecyclerView displaying installed apps
    private lateinit var adapterApps: MonthsAdapter
    // List of installed apps, represented as MonthItem objects
    private var installedAppsList: List<MonthItem> = mutableListOf<MonthItem>()
    // RecyclerView to display the installed apps
    private lateinit var recyclerView: RecyclerView
    // GestureManager for handling user interactions with the RecyclerView
    protected var gestureManager: GestureManager? = null

    /**
     * Called when the activity is first created. Initializes the layout and sets up the RecyclerView.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        // Initialize RecyclerView and set its layout manager
        recyclerView = findViewById(R.id.recyclerView)
        val manager = GridLayoutManager(applicationContext, 4)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = manager

        // Get the list of installed apps and filter it based on a new list
        installedAppsList = getAllInstalledApps()
        val newList = getStringList(applicationContext) // Replace with your actual way of getting newList
        val filteredApps = filterInstalledApps(installedAppsList, newList)
        installedAppsList = filteredApps

        // Initialize the adapter and set it on the RecyclerView
        adapterApps = MonthsAdapter(R.layout.grid_item)
        adapterApps.data = installedAppsList.toMutableList()
        recyclerView.adapter = adapterApps

        // Set up a touch listener for the RecyclerView
        recyclerView.addOnItemTouchListener(RecyclerItemTouchListener(object :
            DefaultItemClickListener<MonthItem>() {

            /**
             * Called when an item in the RecyclerView is clicked.
             * Launches the app associated with the clicked item.
             */
            override fun onItemClick(item: MonthItem, position: Int): Boolean {
                val model = installedAppsList.get(position) as Month
                launchApp(model.packageName)
                return true
            }

            /**
             * Called when an item in the RecyclerView is long-pressed.
             * Adds a header for hidden apps and adds the new list of hidden apps to the adapter.
             */
            override fun onItemLongPress(item: MonthItem, position: Int) {
                if (!hiddenAdded) {
                    adapterApps.add(MonthHeader("Hidden Apps"))
                    hiddenIndex = adapterApps.itemCount - 1
                    for (i in newList) {
                        val packageManager = packageManager
                        val appInfo = packageManager.getApplicationInfo(i, 0)
                        val appLogo = packageManager.getApplicationIcon(appInfo)
                        adapterApps.add(Month(appInfo.packageName, appLogo, appInfo.packageName, openCounter = 0))
                    }
                }
                hiddenAdded = true
            }

            /**
             * Called when an item in the RecyclerView is double-tapped.
             * Displays a toast message.
             */
            override fun onDoubleTap(item: MonthItem, position: Int): Boolean {
                Toast.makeText(applicationContext, "Double tap event on the $position position", Toast.LENGTH_SHORT).show()
                return true
            }
        }))

        // Set up the GridLayoutManager's spanSizeLookup to handle headers and apps differently
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val item = adapterApps.getItem(position)
                return if (item.type === MonthItem.MonthItemType.HEADER) manager.spanCount else 1
            }
        }

        // Initialize the GestureManager and set it up with the RecyclerView
        gestureManager = GestureManager.Builder(recyclerView)
            .setSwipeEnabled(true)
            .setLongPressDragEnabled(true)
            .build()

        // Set up a listener for changes in the adapter's data
        adapterApps.setDataChangeListener(object : GestureAdapter.OnDataChangeListener<MonthItem> {
            /**
             * Called when an item is removed from the adapter.
             * Displays a toast message.
             */
            override fun onItemRemoved
