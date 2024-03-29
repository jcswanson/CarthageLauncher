package com.codesteem.mylauncher.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.codesteem.mylauncher.R
import com.codesteem.mylauncher.models.AppSearchModel

class AppSearchAdapter(
    val activity: Activity,
    val searchBar: CardView,
    val editText: EditText
) : RecyclerView.Adapter<AppSearchAdapter.AppViewHolder>() {
    private var apps: List<AppSearchModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.dialog_list_item, parent, false)
        return AppViewHolder(itemView)
    }
    fun setApps(apps: List<AppSearchModel>) {
        this.apps = apps
        notifyDataSetChanged()
    }
    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
//        val appInfo = installedApps!![position]
//        val packageManager = activity.packageManager

//        val appName = appInfo.name
//        val appLogo = packageManager.getApplicationIcon(appInfo)
        if (apps.isNotEmpty()){

            holder.bind(apps!![position],activity)
        }
    }

    override fun getItemCount(): Int {
        return apps?.size ?: 0
    }

    inner class AppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val appNameTextView: TextView = itemView.findViewById(R.id.appNameTextView)
        private val appIconImageView: ImageView = itemView.findViewById(R.id.appIconImageView)

        fun bind(appSearchModel: AppSearchModel, activity: Activity) {
            appNameTextView.text = appSearchModel.title
            appIconImageView.setImageDrawable(appSearchModel.userImage)
            itemView.setOnClickListener {
                searchBar.visibility = View.GONE
                launchApp(activity, appSearchModel.packageName!!)
            }
        }
    }
    private fun launchApp(context: Context, packageName: String) {
        editText.setText("")
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        intent?.let {
            context.startActivity(it)
        } ?: run {
            // If the intent is null, the app is not launchable (e.g., system apps).
            // You can handle this case based on your requirement.
        }
    }
}