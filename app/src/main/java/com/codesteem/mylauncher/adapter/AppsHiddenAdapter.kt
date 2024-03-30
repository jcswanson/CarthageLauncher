// Package declaration for the custom RecyclerView adapter
package com.codesteem.mylauncher.adapter

// Import necessary classes and libraries
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


// AppsHiddenAdapter class definition with constructor and inheritance from RecyclerView.Adapter
class AppsHiddenAdapter(
    private val context: Context, // Context for accessing system services
    private val appList: MutableList<String>, // List of app package names
    private val selectionHiddenChangeListener: SelectionHiddenChangeListener // Listener for selection hidden changes

) : RecyclerView.Adapter<AppsHiddenAdapter.ViewHolder>() {

    // Declare and initialize a mutable set for storing selected apps
    private val selectedApps = mutableSetOf<String>()

    // Declare and initialize a variable for tracking selection mode
    private var isSelectionMode = false

    // onCreateViewHolder method implementation to inflate the item layout and return a ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate the item layout and return a new ViewHolder instance
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_app, parent, false)
        return ViewHolder(itemView)
    }

    // onBindViewHolder method implementation to bind data to the ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Retrieve the package name, package manager, and app info for the current position
        val packageName = appList[position]
        val packageManager = context.packageManager
        val appInfo = packageManager.getApplicationInfo(packageName, 0)
        val appLogo = packageManager.getApplicationIcon(appInfo)

        // Load the app logo using Glide library
        Glide.with(context).load(appLogo).into(holder.appLogoImageView)

        // Set the visibility and checked state of the checkbox based on the selection mode
        holder.checkBox.visibility = if (isSelectionMode) View.VISIBLE else View.GONE
        holder.checkBox.isChecked = selectedApps.contains(packageName)

        // Set long click listener for entering selection mode and toggling selection
        holder.itemView.setOnLongClickListener {
            isSelectionMode = true
            toggleSelection(packageName, holder.checkBox)
            notifyDataSetChanged()
            true
        }

        // Set click listener for launching the app or toggling selection
        holder.itemView.setOnClickListener {
            if (isSelectionMode) {
                toggleSelection(packageName, holder.checkBox)
            } else {
                launchApp(packageName)
            }
        }

        // Set checked change listener for the checkbox to toggle selection
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            toggleSelection(packageName, holder.checkBox)
        }
    }

    // Method for toggling selection and updating the UI and data structures
    private fun toggleSelection(packageName: String, checkBox: CheckBox) {
        // Toggle the selectedApps set and update the checkbox state
        if (selectedApps.contains(packageName)) {
            selectedApps.remove(packageName)
            checkBox.isChecked = false
        } else {
            selectedApps.add(packageName)
            checkBox.isChecked = true
        }

        // Notify the listener about the selection hidden change
        selectionHiddenChangeListener.onSelectionHiddenChanged(selectedApps.size, packageName)
    }

    // Method for retrieving the list of hidden selected apps
    fun getHiddenSelectedApps(): List<String> {
        // Return the selectedApps set as a list
        return selectedApps.toList()
    }

    // Method for exiting selection mode and updating the app list and UI
    fun exitHiddenSelectionMode(): List<String> {
        // Create a new app list, remove selected apps, and clear the selectedApps set
        val newAppList = appList.toMutableList() // Convert to MutableList
        newAppList.removeAll(selectedApps)
        isSelectionMode = false
        selectedApps.clear()

        // Notify the adapter about the data set change
        notifyDataSetChanged()

        // Return the updated app list
        return newAppList
    }

    // Method for launching the app using the package name
    private fun launchApp(packageName: String) {
        // Retrieve the launch intent for the package name, and start the activity if it's not null
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        intent?.let {
            context.startActivity(it)
        } ?: run {
            // If the intent is null, show a Toast message
            Toast.makeText(context, "App not launchable", Toast.LENGTH_SHORT).show()
        }
    }

    // Method for getting the item count based on the appList size
    override fun getItemCount(): Int {
        // Return the size of the appList
        return appList.size
    }

    // Inner class for defining the ViewHolder
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Declare and initialize the views using the itemView
        val appLogoImageView: ImageView = itemView.findViewById(R.id.appLogoImageView)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkbox)
   
