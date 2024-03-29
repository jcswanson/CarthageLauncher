package com.codesteem.mylauncher.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codesteem.mylauncher.R
import com.codesteem.mylauncher.SelectionChangeListener


class AppsAdapter(
    private val context: Context,
    private val appList: MutableList<String>,
    private val selectionChangeListener: SelectionChangeListener

) : RecyclerView.Adapter<AppsAdapter.ViewHolder>() {
    private val appCounters = mutableMapOf<String, Int>()

    private val selectedApps = mutableSetOf<String>()
    private var isSelectionMode = false
    // Initialize appCounters with initial values

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_app, parent, false)
        return ViewHolder(itemView)
    }
    // Step 1: Add a method to update the counter for a specific app
    fun updateCounterForApp(packageName: String, count: Int) {
        appCounters[packageName] = count
        notifyDataSetChanged() // Refresh the RecyclerView to show the updated counter
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val packageName = appList[position]
        val packageManager = context.packageManager
        val appInfo = packageManager.getApplicationInfo(packageName, 0)
        val appLogo = packageManager.getApplicationIcon(appInfo)

        Glide.with(context).load(appLogo).into(holder.appLogoImageView)
        val countR=appCounters[packageName]
        if (countR!=null&&countR!=0){
            holder.tvCounter.visibility=View.VISIBLE
            holder.tvCounter.text = (countR).toString()
        }else{
            holder.tvCounter.visibility=View.GONE

        }
        holder.checkBox.visibility = if (isSelectionMode) View.VISIBLE else View.GONE
        holder.checkBox.isChecked = selectedApps.contains(packageName)
// Example: Update the counter for the app when needed

        holder.itemView.setOnLongClickListener {
            isSelectionMode = true
            toggleSelection(packageName, holder.checkBox)
            notifyDataSetChanged()
            true
        }

        holder.itemView.setOnClickListener {
            if (isSelectionMode) {
                toggleSelection(packageName, holder.checkBox)
            } else {
                launchApp(packageName)
            }
        }
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            toggleSelection(packageName, holder.checkBox)
        }
    }

    private fun toggleSelection(packageName: String, checkBox: CheckBox) {
        if (selectedApps.contains(packageName)) {
            selectedApps.remove(packageName)
            checkBox.isChecked = false
        } else {
            selectedApps.add(packageName)
            checkBox.isChecked = true
        }

        selectionChangeListener.onSelectionChanged(selectedApps.size,packageName)
    }


    fun getSelectedApps(): List<String> {
        return selectedApps.toList()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun exitSelectionMode(): List<String> {
        val selectedApps1 = selectedApps.toList() // Convert to List
        appList.removeAll(selectedApps)
        isSelectionMode = false
        selectedApps.clear()
        notifyDataSetChanged()
        return selectedApps1
    }


    private fun launchApp(packageName: String) {
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        intent?.let {
            context.startActivity(it)
        } ?: run {
            // If the intent is null, the app is not launchable.
            Toast.makeText(context, "App not launchable", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return appList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val appLogoImageView: ImageView = itemView.findViewById(R.id.appLogoImageView)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkbox)
        val tvCounter: TextView = itemView.findViewById(R.id.tvCounter)
    }


}

