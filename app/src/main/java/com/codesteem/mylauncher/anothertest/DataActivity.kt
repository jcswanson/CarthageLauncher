package com.codesteem.mylauncher.anothertest

import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codesteem.mylauncher.R
import com.codesteem.mylauncher.test.Month
import com.codesteem.mylauncher.test.MonthItem
import com.codesteem.mylauncher.util.CustomDrawable
import java.util.*

/**
 */

class DataActivity : AppCompatActivity(), Listener {

    private lateinit var tvEmptyListTop: TextView
    private lateinit var tvEmptyListMid: TextView
    private lateinit var tvEmptyListBottom: TextView
    private lateinit var rvTop: RecyclerView
    private lateinit var rvBottom: RecyclerView
    private lateinit var rvMid: RecyclerView
    private var installedAppsList: List<Drawable> =mutableListOf<Drawable>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data)
        rvTop=findViewById<RecyclerView>(R.id.rvTop)
        rvBottom=findViewById<RecyclerView>(R.id.rvBottom)
        rvMid=findViewById<RecyclerView>(R.id.rvMid)
        tvEmptyListTop=findViewById<TextView>(R.id.tvEmptyListTop)
        tvEmptyListBottom=findViewById<TextView>(R.id.tvEmptyListBottom)
        tvEmptyListMid=findViewById<TextView>(R.id.tvEmptyListMid)
        setTopRecyclerView()
        setBottomRecyclerView()
        setMidRecyclerView()

        tvEmptyListTop.setVisibility(View.GONE)
        tvEmptyListBottom.setVisibility(View.GONE)
        tvEmptyListMid.setVisibility(View.GONE)
    }

    private fun setTopRecyclerView() {
        rvTop.setHasFixedSize(true)
        rvTop.isNestedScrollingEnabled = false

//        rvTop.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvTop.layoutManager = GridLayoutManager(this, 4)
        installedAppsList = getAllInstalledApps()
        val topList: MutableList<Drawable> = ArrayList()
//        val customDrawable = CustomDrawable()
//        topList.add(customDrawable)

        topList.addAll(installedAppsList)


        val topListAdapter = MainAdapter(topList, this,applicationContext)
        rvTop.adapter = topListAdapter
//        tvEmptyListTop.setOnDragListener(topListAdapter.dragInstance)
//        rvTop.setOnDragListener(topListAdapter.dragInstance)
    }
    private fun setBottomRecyclerView() {
        rvBottom.setHasFixedSize(true)
//        rvBottom.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvBottom.layoutManager = GridLayoutManager(this, 4)
        val customDrawable = CustomDrawable()

        val bottomList: MutableList<Drawable> = ArrayList()
        bottomList.add(customDrawable)
        val bottomListAdapter = MainAdapter(bottomList, this,applicationContext)
        rvBottom.adapter = bottomListAdapter
        tvEmptyListBottom.setOnDragListener(bottomListAdapter.dragInstance)
        rvBottom.setOnDragListener(bottomListAdapter.dragInstance)

        bottomList.removeAt(0)
        bottomListAdapter.notifyDataSetChanged()
    }
    private fun setMidRecyclerView() {
        rvMid.setHasFixedSize(true)
//        rvBottom.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvMid.layoutManager = GridLayoutManager(this, 4)
        val customDrawable = CustomDrawable()

        val midList: MutableList<Drawable> = ArrayList()
        midList.add(customDrawable)
        val midListAdapter = MainAdapter(midList, this,applicationContext)
        rvMid.adapter = midListAdapter
        tvEmptyListMid.setOnDragListener(midListAdapter.dragInstance)
        rvMid.setOnDragListener(midListAdapter.dragInstance)

        midList.removeAt(0)
        midListAdapter.notifyDataSetChanged()
    }
    private fun getAllInstalledApps(): List<Drawable> {
        val packageManager = packageManager
        val installedApps = mutableListOf<Drawable>()

        val apps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        for (appInfo in apps) {
            if (packageManager.getLaunchIntentForPackage(appInfo.packageName) != null) {
                val appLogo = packageManager.getApplicationIcon(appInfo)
                installedApps.add(appLogo)
            }

        }
        return installedApps
    }

    override fun setEmptyListTop(visibility: Boolean) {
        tvEmptyListTop.visibility = if (visibility) View.VISIBLE else View.GONE
        rvTop.visibility = if (visibility) View.GONE else View.VISIBLE
    }

    override fun setEmptyListBottom(visibility: Boolean) {
        tvEmptyListBottom.visibility = if (visibility) View.VISIBLE else View.GONE
        rvBottom.visibility = if (visibility) View.GONE else View.VISIBLE
    }
    override fun setEmptyListMid(visibility: Boolean) {
        tvEmptyListMid.visibility = if (visibility) View.VISIBLE else View.GONE
        rvMid.visibility = if (visibility) View.GONE else View.VISIBLE
    }
}