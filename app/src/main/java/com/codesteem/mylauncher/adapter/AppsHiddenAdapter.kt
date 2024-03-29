package com.codesteem.mylauncher.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codesteem.mylauncher.R
import com.codesteem.mylauncher.SelectionChangeListener
import com.codesteem.mylauncher.SelectionHiddenChangeListener


class AppsHiddenAdapter(
    private val context: Context,
    private val appList: MutableList<String>,
    private val selectionHiddenChangeListener: SelectionHiddenChangeListener

) : RecyclerView.Adapter<AppsHiddenAdapter.ViewHolder>() {

    private val selectedApps = mutableSetOf<String>()
    private var isSelectionMode = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_app, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val packageName = appList[position]
        val packageManager = context.packageManager
        val appInfo = packageManager.getApplicationInfo(packageName, 0)
        val appLogo = packageManager.getApplicationIcon(appInfo)

        Glide.with(context).load(appLogo).into(holder.appLogoImageView)

        holder.checkBox.visibility = if (isSelectionMode) View.VISIBLE else View.GONE
        holder.checkBox.isChecked = selectedApps.contains(packageName)

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

        selectionHiddenChangeListener.onSelectionHiddenChanged(selectedApps.size,packageName)
    }


    fun getHiddenSelectedApps(): List<String> {
        return selectedApps.toList()
    }

    fun exitHiddenSelectionMode(): List<String> {
        val newAppList = appList // Convert to List
        newAppList.removeAll(selectedApps)
        isSelectionMode = false
        selectedApps.clear()
        notifyDataSetChanged()
        return newAppList
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
    }

}

