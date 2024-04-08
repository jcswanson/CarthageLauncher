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
 * This activity displays a grid of installed apps and allows the user to interact with them.
 * It also handles the logic for hiding and unhiding apps.
 */
class TestActivity : AppCompatActivity() {
    // Tracks the index of the "Hidden Apps" header in the adapter's data list
    private var hiddenIndex: Int = -1
    // Tracks whether the "Hidden Apps" header has been added to the adapter's data list
    private var hiddenAdded: Boolean = false
    // The adapter for the RecyclerView that displays the installed apps
    private lateinit var adapterApps: MonthsAdapter
    // The list of installed apps, represented as MonthItem objects
    private var installedAppsList: List<MonthItem> = mutableListOf<MonthItem>()
    // The RecyclerView that displays the installed apps
    private lateinit var recyclerView: RecyclerView
    // The GestureManager that handles user gestures on the RecyclerView
    protected var gestureManager: GestureManager? = null

    // Override onCreate method
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        // Initialize RecyclerView and set its layout manager
        recyclerView = findViewById(R.id.recyclerView)
        val manager = GridLayoutManager(applicationContext, 4)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = manager

        // Get the list of installed apps and filter it based on the newList
        installedAppsList = getAllInstalledApps()
        val newList = getStringList(applicationContext) // Replace with your actual way of getting newList
        val filteredApps = filterInstalledApps(installedAppsList, newList)
        installedAppsList = filteredApps

        // Initialize the adapter with the filtered installed apps list
        adapterApps = MonthsAdapter(R.layout.grid_item)
        adapterApps.data = installedAppsList.toMutableList()
        recyclerView.adapter = adapterApps

        // Add a RecyclerItemTouchListener to handle user gestures on the RecyclerView
        recyclerView.addOnItemTouchListener(RecyclerItemTouchListener(object :
            DefaultItemClickListener<MonthItem>() {

            // Handle single click events on the MonthItem objects
            override fun onItemClick(item: MonthItem, position: Int): Boolean {
                // Launch the app associated with the clicked MonthItem
                val model = installedAppsList.get(position) as Month
                launchApp(model.packageName)
                return true
            }

            // Handle long press events on the MonthItem objects
            override fun onItemLongPress(item: MonthItem, position: Int) {
                if (!hiddenAdded) {
                    // Add the "Hidden Apps" header to the adapter's data list
                    adapterApps.add(MonthHeader("Hidden Apps"))
                    hiddenIndex = adapterApps.itemCount - 1
                    // Add the apps from newList to the adapter's data list
                    for (i in newList) {
                        val packageManager = packageManager
                        val appInfo = packageManager.getApplicationInfo(i, 0)
                        val appLogo = packageManager.getApplicationIcon(appInfo)
                        adapterApps.add(Month(appInfo.packageName, appLogo, appInfo.packageName, openCounter = 0))
                    }
                }
                hiddenAdded = true
            }

            // Handle double tap events on the MonthItem objects
            override fun onDoubleTap(item: MonthItem, position: Int): Boolean {
                // Handle double tap events
                return true
            }
        }))

        // Set the span size lookup for the GridLayoutManager
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val item = adapterApps.getItem(position)
                return if (item.type === MonthItem.MonthItemType.HEADER) manager.spanCount else 1
            }
        }

        // Initialize the GestureManager with the RecyclerView
        gestureManager = GestureManager.Builder(recyclerView)
            .setSwipeEnabled(true)
            .setLongPressDragEnabled(true)
            .build()

        // Set the OnDataChangeListener for the adapter
        adapterApps.setDataChangeListener(object : GestureAdapter.OnDataChangeListener<MonthItem> {
            // Handle item removal events
            override fun onItemRemoved(item: MonthItem, position: Int, direction: Int) {
                // Handle item removal events
            }

            // Handle item reorder events
            override fun onItemReorder(item: MonthItem, fromPos: Int, toPos: Int) {
                // Update the newList based on the reordered item's position
                hiddenIndex = findMonthHeaderIndexByName("Hidden Apps", adapterApps
