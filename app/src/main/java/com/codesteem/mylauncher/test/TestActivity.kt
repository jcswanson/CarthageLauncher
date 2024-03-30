package com.codesteem.mylauncher.test

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codesteem.mylauncher.R
import com.codesteem.mylauncher.models.AppInfo
import com.codesteem.mylauncher.util.MySharedPreferences
import com.codesteem.mylauncher.util.MySharedPreferences.getStringList
import com.codesteem.mylauncher.gesture.DefaultItemClickListener
import com.codesteem.mylauncher.gesture.GestureAdapter
import com.codesteem.mylauncher.gesture.GestureManager
import com.codesteem.mylauncher.gesture.RecyclerItemTouchListener

class TestActivity : AppCompatActivity() {

    private lateinit var adapterApps: MonthsAdapter
    private val installedAppsList: MutableList<MonthItem> = mutableListOf()
    private lateinit var recyclerView: RecyclerView
    private lateinit var gestureManager: GestureManager

    private var hiddenIndex = -1
    private var hiddenAdded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        recyclerView = findViewById(R.id.recyclerView)
        val manager = GridLayoutManager(this, 4)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = manager

        installedAppsList.addAll(getAllInstalledApps())
        val newList = getStringList(this) // Replace with your actual way of getting newList
        installedAppsList.addAll(filterInstalledApps(installedAppsList, newList))

        adapterApps = MonthsAdapter(R.layout.grid_item, installedAppsList)
        recyclerView.adapter = adapterApps

        recyclerView.addOnItemTouchListener(
            RecyclerItemTouchListener(
                object : DefaultItemClickListener<MonthItem>() {

                    override fun onItemClick(item: MonthItem, position: Int): Boolean {
                        launchApp(item.packageName)
                        return true
                    }

                    override fun onItemLongPress(item: MonthItem, position: Int) {
                        if (!hiddenAdded) {
                            adapterApps.addHeader("Hidden Apps")
                            hiddenIndex = adapterApps.itemCount - 1
                            newList.forEach { packageName ->
                                val appLogo = packageManager.getApplicationIcon(packageName)
                                adapterApps.addApp(AppInfo(packageName, appLogo))
                            }
                            hiddenAdded = true
                        }
                    }

                    override fun onDoubleTap(item: MonthItem, position: Int): Boolean {
                        Toast.makeText(
                            applicationContext,
                            "Double tap event on the $position position",
                            Toast.LENGTH_SHORT
                        ).show()
                        return true
                    }
                }
            )
        )

        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (adapterApps.isHeader(position)) manager.spanCount else 1
            }
        }

        gestureManager = GestureManager.Builder(recyclerView)
            .setSwipeEnabled(true)
            .setLongPressDragEnabled(true)
            .build()

        adapterApps.registerDataSetChangeListener(object :
            GestureAdapter.OnDataChangeListener<MonthItem> {
            override fun onItemRemoved(item: MonthItem, position: Int) {
                Toast.makeText(applicationContext, "Item removed: $item", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getAllInstalledApps(): List<MonthItem> {
        // Your logic to get all installed apps
        return emptyList()
    }

    private fun filterInstalledApps(
        installedAppsList: List<MonthItem>,
        newList: List<String>
    ): List<MonthItem> {
        // Your logic to filter installed apps based on newList
        return emptyList()
    }

    private fun launchApp(packageName: String) {
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        if (intent != null) {
            startActivity(intent)
        } else {
            Log.e("TestActivity", "Unable to launch app with package name: $packageName")
        }
    }
}
