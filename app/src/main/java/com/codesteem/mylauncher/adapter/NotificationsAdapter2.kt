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


class NotificationsAdapter2(
    private val onItemDeleteListener: OnItemDeleteListener
) : ListAdapter<NotificationsModel, NotificationsAdapter2.NotificationsViewHolder>(
    NotificationItemCallBack()
),
    ItemTouchHelperAdapter {

    class NotificationItemCallBack: DiffUtil.ItemCallback<NotificationsModel>() {
        override fun areItemsTheSame(
            oldItem: NotificationsModel,
            newItem: NotificationsModel
        ): Boolean {
            return oldItem.time == newItem.time
        }

        override fun areContentsTheSame(
            oldItem: NotificationsModel,
            newItem: NotificationsModel
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationsViewHolder {
        val binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationsViewHolder(binding, onNotificationClickListener)
    }

    override fun onBindViewHolder(holder: NotificationsViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onItemSwiped(position: Int, direction: Int, viewHolder: RecyclerView.ViewHolder) {
        if (position in 0 until currentList.size) {
            /*val list = currentList
            list.removeAt(position)
            submitList(list)
            notifyItemRemoved(position)*/
            onItemDeleteListener.onItemDeleted(position, currentList[position])
        }
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    private var onNotificationClickListener: ((notification: NotificationsModel) -> Unit)? = null

    fun setOnNotificationClickListener(onNotificationClick: (notification: NotificationsModel) -> Unit) {
        onNotificationClickListener = onNotificationClick
    }

    class NotificationsViewHolder(
        private val binding: ItemNotificationBinding,
        private val notificationListener: ((notification: NotificationsModel) -> Unit)?
    ) :RecyclerView.ViewHolder(binding.root) {
        fun bind(
            notiModel: NotificationsModel
        ) {
            with(itemView) {

                binding.tvName.text = notiModel.title
                binding.tvType.text = notiModel.desc
                binding.tvTime.text = "• "+notiModel.app_name+" • "+notiModel.time
                binding.tvAlarmTime.text = notiModel.alarmTime

                Glide.with(context).load(notiModel.app_icon).into(binding.ivAppIcon)
//                binding.ivThumbnail.setImageBitmap(notiModel.mediaImage)

                if (notiModel.notificationType=="alarm"){
                    binding.linAlarmTime.visibility=View.VISIBLE
                    binding.ivThumbnail.visibility=View.GONE
                    binding.tvTime.visibility=View.GONE
                }else{
                    binding.linAlarmTime.visibility=View.GONE
//                    binding.ivThumbnail.visibility=View.VISIBLE
                    binding.tvTime.visibility=View.VISIBLE

                }
                itemView.setOnClickListener {
                    /*if (!notiModel.relatedAppPackageName.isNullOrEmpty()) {
                        launchApp(context, notiModel.relatedAppPackageName)
                    }*/
                    notificationListener?.let { it(notiModel) }
                }

            }
        }

        private fun launchApp(context: Context, packageName: String) {
            val packageManager = context.packageManager
            val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
            launchIntent?.let {
                context.startActivity(it)
            }
        }
    }

    private fun getNotiData(sbn: StatusBarNotification, context: Context): NotificationsModel {
        val notification = sbn.notification

        val title = notification.extras.getString(Notification.EXTRA_TITLE)
        val text = notification.extras.getString(Notification.EXTRA_TEXT)

        val notificationTime = sbn.postTime // Notification timestamp in milliseconds

        // Get the notification icon (small icon)
        val smallIconBitmap: Bitmap? = sbn.notification.smallIcon?.loadDrawable(context)?.toBitmap()
        val smallIconByteArray = smallIconBitmap?.let { bitmapToByteArray(it) }

        // You can also get the large icon (if available)
        val packageName = sbn.packageName
        val appInfo: AppInfo? = getApplicationInfo(context, packageName)
        val appIcon: Drawable = byteArrayToDrawable(appInfo!!.appIconByteArray, context.resources)
        val iconAsString = Base64.encodeToString(appInfo.appIconByteArray, Base64.DEFAULT)

        // Send all notification details via broadcast
        val intent = Intent("ACTION_NOTIFICATION_RECEIVED")
        intent.putExtra("title", title)
        intent.putExtra("text", text)
        intent.putExtra("pkg", packageName)
        intent.putExtra("app_info", appInfo)
        intent.putExtra("heads-up", isHeadsUpNotification(notification))
        intent.putExtra("notificationTime", notificationTime)
        intent.putExtra("smallIcon", smallIconByteArray)
        intent.putExtra("notiKey", sbn.key)

        return NotificationsModel(
            formatTimeFromTimestamp(notificationTime),
            "",
            title,
            text,
            smallIconBitmap!!,
            "simple",
            "", packageName, appInfo.appName, iconAsString, sbn.key
        )
    }

    fun formatTimeFromTimestamp(timestamp: Long): String {
        val pattern = "h:mm a"
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        return sdf.format(timestamp)
    }

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    private fun getApplicationInfo(context: Context, packageName: String): AppInfo? {
        val packageManager: PackageManager = context.packageManager

        return try {
            val appInfo: ApplicationInfo = packageManager.getApplicationInfo(packageName, 0)
            val appName: String = packageManager.getApplicationLabel(appInfo).toString()
            val appIcon: Drawable = packageManager.getApplicationIcon(appInfo)
            val appIconByteArray = drawableToByteArray(appIcon)
            AppInfo(appName, appIconByteArray)
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
    }

    private fun drawableToByteArray(drawable: Drawable): ByteArray {
        val bitmap = (drawable).toBitmap()
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    private fun byteArrayToDrawable(byteArray: ByteArray, resources: Resources): Drawable {
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        return BitmapDrawable(resources, bitmap)
    }

    fun isHeadsUpNotification(notification: Notification): Boolean {
        // Check if the priority is set to PRIORITY_HIGH or PRIORITY_MAX
        return (notification.priority == NotificationCompat.PRIORITY_HIGH ||
                notification.priority == NotificationCompat.PRIORITY_MAX)
    }

    interface OnItemDeleteListener {
        fun onItemDeleted(position: Int, item: NotificationsModel)
    }

    fun filterList(filterlist: ArrayList<NotificationsModel>) {
        // below line is to add our filtered
        // list in our course array list.
        //mList = filterlist
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged()
    }
}