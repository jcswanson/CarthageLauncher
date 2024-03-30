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
 * NotificationsAdapter2 is a RecyclerView adapter for displaying a list of notifications.
 * It extends ListAdapter, which provides a standard way of handling data updates and diffing.
 * The adapter also implements ItemTouchHelperAdapter, allowing it to handle swipe-to-dismiss gestures.
 */
class NotificationsAdapter2(
    private val onItemDeleteListener: OnItemDeleteListener
) : ListAdapter<NotificationsModel, NotificationsAdapter2.NotificationsViewHolder>(
    NotificationItemCallBack()
), ItemTouchHelperAdapter {

    /**
     * NotificationItemCallBack is a DiffUtil.ItemCallback implementation for comparing NotificationsModel objects.
     * It is used by ListAdapter to calculate the differences between old and new lists and update the RecyclerView accordingly.
     */
    class NotificationItemCallBack : DiffUtil.ItemCallback<NotificationsModel>() {
        override fun areItemsTheSame(
            oldItem: NotificationsModel,
            newItem: NotificationsModel
        ): Boolean {
            // Compares the time field of NotificationsModel objects to determine if they represent the same item.
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

    /**
     * onCreateViewHolder is called when RecyclerView needs a new ViewHolder for displaying an item.
     * It inflates the layout for a single notification item and returns a NotificationsViewHolder instance.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationsViewHolder {
        val binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationsViewHolder(binding, onNotificationClickListener)
    }

    /**
     * onBindViewHolder is called when RecyclerView needs to display a new item or update an existing one.
     * It binds the NotificationsModel object to the corresponding views in the NotificationsViewHolder.
     */
    override fun onBindViewHolder(holder: NotificationsViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    /**
     * onItemSwiped is called when a swipe-to-dismiss gesture is detected on an item.
     * It removes the item from the current list and notifies the adapter of the change.
     */
    override fun onItemSwiped(position: Int, direction: Int, viewHolder: RecyclerView.ViewHolder) {
        if (position in 0 until currentList.size) {
            onItemDeleteListener.onItemDeleted(position, currentList[position])
        }
    }

    /**
     * onItemMoved is called when an item is moved to a new position in the RecyclerView.
     * It is empty in this implementation, as the adapter does not support item reordering.
     */
    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        // Do nothing
    }

    /**
     * getItemCount returns the number of items in the current list.
     */
    override fun getItemCount(): Int {
        return currentList.size
    }

    /**
     * onNotificationClickListener is a callback interface for handling click events on notification items.
     */
    private var onNotificationClickListener: ((notification: NotificationsModel) -> Unit)? = null

    /**
     * setOnNotificationClickListener sets the callback for handling click events on notification items.
     */
    fun setOnNotificationClickListener(onNotificationClick: (notification: NotificationsModel) -> Unit) {
        onNotificationClickListener = onNotificationClick
    }

    /**
     * NotificationsViewHolder is a ViewHolder for a single notification item.
     * It contains a reference to the layout binding and handles click events and data binding.
     */
    class NotificationsViewHolder(
        private val binding: ItemNotificationBinding,
        private val notificationListener: ((notification: NotificationsModel) -> Unit)?
    ) : RecyclerView.ViewHolder(binding.root) {

        /**
         * bind binds the NotificationsModel object to the corresponding views in the NotificationsViewHolder.
         */
        fun bind(notiModel: NotificationsModel) {
            with(itemView) {
                // Binds the title, desc, time, app_name, and app_icon fields of the NotificationsModel object to the corresponding views.
                binding.tvName.text = notiModel.title
                binding.tvType.text = notiModel.desc
                binding.tvTime.text = "• " + notiModel.app_name + " • " + notiModel.time
                binding
