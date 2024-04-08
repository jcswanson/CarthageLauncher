package com.codesteem.mylauncher.adapter

import android.app.Notification
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.service.notification.StatusBarNotification
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codesteem.mylauncher.databinding.ItemNotificationBinding
import com.codesteem.mylauncher.models.AppInfo
import com.codesteem.mylauncher.models.NotificationsModel
import com.codesteem.mylauncher.util.ItemTouchHelperAdapter
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * NotificationsAdapter2 is a RecyclerView adapter for displaying notifications in a list.
 * It uses DiffUtil for efficient list updates and implements ItemTouchHelperAdapter for swipe-to-dismiss functionality.
 */
class NotificationsAdapter2(
    private val onItemDeleteListener: OnItemDeleteListener
) : ListAdapter<NotificationsModel, NotificationsAdapter2.NotificationsViewHolder>(
    NotificationItemCallBack()
),
    ItemTouchHelperAdapter {

    /**
     * NotificationItemCallBack is a DiffUtil.ItemCallback implementation for comparing NotificationsModel objects.
     */
    class NotificationItemCallBack : DiffUtil.ItemCallback<NotificationsModel>() {
        override fun areItemsTheSame(
            oldItem: NotificationsModel,
            newItem: NotificationsModel
        ): Boolean {
            // Compares the time field of NotificationsModel objects to determine if they are the same item.
            return oldItem.time == newItem.time
        }

        override fun areContentsTheSame(
            oldItem: NotificationsModel,
            newItem: NotificationsModel
        ): Boolean {
            // Compares all fields of NotificationsModel objects to determine if their contents are the same.
            return oldItem == newItem
        }
    }

    // ... (rest of the code)
}


/**
 * NotificationsViewHolder is a RecyclerView.ViewHolder for displaying a single notification item.
 */
class NotificationsViewHolder(
    private val binding: ItemNotificationBinding,
    private val notificationListener: ((notification: NotificationsModel) -> Unit)?
) : RecyclerView.ViewHolder(binding.root) {

    /**
     * Binds the given NotificationsModel object to the view holder.
     */
    fun bind(notiModel: NotificationsModel) {
        with(itemView) {
            // Binds the notification title, description, time, alarm time, and app icon to the corresponding views.
            binding.tvName.text = notiModel.title
            binding.tvType.text = notiModel.desc
            binding.tvTime.text = "• " + notiModel.app_name + " • " + notiModel.time
            binding.tvAlarmTime.text = notiModel.alarmTime

            Glide.with(context).load(notiModel.app_icon).into(binding.ivAppIcon)
            binding.ivThumbnail.visibility = if (notiModel.notificationType == "alarm") View.GONE else View.VISIBLE
            binding.tvTime.visibility = if (notiModel.notificationType == "alarm") View.GONE else View.VISIBLE

            // Sets an OnClickListener for the item view, which launches the related app if available.
            setOnClickListener {
                notificationListener?.let { it(notiModel) }
            }
        }
    }

    // ... (rest of the code)
}
