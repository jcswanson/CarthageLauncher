package com.codesteem.mylauncher.adapter

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codesteem.mylauncher.R
import com.codesteem.mylauncher.database.NotificationEntity
import com.codesteem.mylauncher.databinding.ItemNotificationBinding
import com.codesteem.mylauncher.util.ItemTouchHelperAdapter
import com.codesteem.mylauncher.util.NotificationListener
import java.text.SimpleDateFormat
import java.util.Locale


class NotificationsAdapter(
    private val onItemDeleteListener: OnItemDeleteListener
) : ListAdapter<NotificationEntity, NotificationsAdapter.NotificationsViewHolder>(
    NotificationItemCallBack()
),
    ItemTouchHelperAdapter {

    class NotificationItemCallBack: DiffUtil.ItemCallback<NotificationEntity>() {
        override fun areItemsTheSame(
            oldItem: NotificationEntity, newItem: NotificationEntity
        ): Boolean {
            return oldItem.timeStamp == newItem.timeStamp
        }

        override fun areContentsTheSame(
            oldItem: NotificationEntity,
            newItem: NotificationEntity
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationsViewHolder {
        val binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationsViewHolder(binding, onNotificationClickListener, onActionClickListener)
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

    private var onNotificationClickListener: ((notification: NotificationEntity) -> Unit)? = null

    fun setOnNotificationClickListener(onNotificationClick: (notification: NotificationEntity) -> Unit) {
        onNotificationClickListener = onNotificationClick
    }

    private var onActionClickListener: ((notification: NotificationEntity) -> Unit)? = null

    fun setOnActionClickListener(onActionClick: (notification: NotificationEntity) -> Unit) {
        onActionClickListener = onActionClick
    }

    class NotificationsViewHolder(
        private val binding: ItemNotificationBinding,
        private val notificationListener: ((notification: NotificationEntity) -> Unit)?,
        private val onActionClick: ((notification: NotificationEntity) -> Unit)?
    ) :RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(
            notiModel: NotificationEntity
        ) {
            with(itemView) {
                binding.replyEt.text.clear()
                binding.tvName.text = notiModel.title
                binding.tvType.text = notiModel.text
                binding.tvTime.text = "• "+notiModel.appInfo?.appName+" • "+formatTimeFromTimestamp(notiModel.timeStamp!!)
                //binding.tvAlarmTime.text = notiModel.alarmTime

                Glide.with(context).load(notiModel.appInfo?.appIconByteArray).into(binding.ivAppIcon)
//                binding.ivThumbnail.setImageBitmap(notiModel.mediaImage)

                binding.linAlarmTime.visibility=View.GONE
//                    binding.ivThumbnail.visibility=View.VISIBLE
                binding.tvTime.visibility=View.VISIBLE
               /* if (notiModel.notificationType=="alarm"){
                    binding.linAlarmTime.visibility=View.VISIBLE
                    binding.ivThumbnail.visibility=View.GONE
                    binding.tvTime.visibility=View.GONE
                }else{
                    binding.linAlarmTime.visibility=View.GONE
//                    binding.ivThumbnail.visibility=View.VISIBLE
                    binding.tvTime.visibility=View.VISIBLE

                }*/

                if (notiModel.actions.find { it.equals("reply", true) || it.equals("quick reply", true)} != null) {
                    binding.replyLayout.visibility = View.VISIBLE
                } else {
                    binding.replyLayout.visibility = View.GONE
                }

                setupActions(
                    notiModel,
                    notiModel.actions,
                    binding.bottomLinearLayout,
                    onActionClick
                )
                setupReply(
                    notiModel,
                    binding.replyLayout,
                    binding.replyEt,
                    binding.replyBtn,
                    onActionClick
                )
                itemView.setOnClickListener {
                    /*notiModel.appInfo?.appName?.let { name ->
                        val packageName = getPackageNameByName(context, notiModel.appInfo?.appName)
                        if (!packageName.isNullOrEmpty()) {
                            launchApp(context, packageName)
                        }

                    }*/
                    notificationListener?.let { it(notiModel) }

                }


                binding.expand.setOnClickListener {
                    notiModel.isExpanded = !notiModel.isExpanded
                    if (notiModel.isExpanded) {
                        binding.expand.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_up_arrow))
                        binding.expand.setColorFilter(ContextCompat.getColor(context, R.color.black))
                        binding.tvType.maxLines = Integer.MAX_VALUE
                    } else {
                        binding.expand.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_down_arrow))
                        binding.expand.setColorFilter(ContextCompat.getColor(context, R.color.black))
                        binding.tvType.maxLines = 1
                    }
                }
            }
        }


        private fun setupActions(
            notification: NotificationEntity,
            actions: List<String>,
            actionsLayout: LinearLayout,
            onActionClick: ((notification: NotificationEntity) -> Unit)?
        ) {
            actionsLayout.removeAllViews()

            actions.forEach { action ->
                val button = Button(actionsLayout.context)
                if (action.equals("reply", true) || action.equals("quick reply", true)) return@forEach
                button.text = action
                actionsLayout.addView(button)
                button.setOnClickListener {
                    NotificationListener.instance?.myNotifications?.find { it.key == notification.id }?.let { sbn ->
                        sbn.notification.actions.find { it.title == action }?.let { action ->
                            val actionIntent = action.actionIntent
                            Log.e("actionIntent", actionIntent.toString())
                            try {
                                actionIntent?.send()
                                onActionClick?.invoke(notification)
                            } catch (e: PendingIntent.CanceledException) {
                                Log.e("NotificationListener", "Error launching PendingIntent: $e")
                            }
                        }
                    }
                }
            }
        }

        private fun setupReply(
            notification: NotificationEntity,
            replyLayout: ConstraintLayout,
            editText: EditText,
            sendBtn: ImageView,
            onActionClick: ((notification: NotificationEntity) -> Unit)?
        ) {
            NotificationListener.instance?.myNotifications?.find { it.key == notification.id }?.let { sbn ->
                val replyAction = sbn.notification.actions.find {
                    it.title.toString().equals("reply", true) || it.title.toString()
                        .equals("quick reply", true)
                }
                if (replyAction != null) {
                    sendBtn.setOnClickListener {
                        val text = editText.text?.toString().orEmpty()
                        if (text.isNotBlank()) {
                            sendBtn.isClickable = true
                            NotificationListener.instance?.reply(text)
                            onActionClick?.invoke(notification)
                        }
                    }
                    replyLayout.visibility = View.VISIBLE

                } else {
                    replyLayout.visibility = View.GONE
                }
            }
        }

        private fun getPackageNameByName(context: Context, appName: String): String? {
            val packageManager: PackageManager = context.packageManager
            val installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

            for (appInfo in installedApps) {
                val label = packageManager.getApplicationLabel(appInfo).toString()
                if (label == appName) {
                    return appInfo.packageName
                }
            }

            // App not found
            return null
        }

        private fun launchApp(context: Context, packageName: String) {
            val packageManager = context.packageManager
            val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
            launchIntent?.let {
                context.startActivity(it)
            }
        }

        fun formatTimeFromTimestamp(timestamp: Long): String {
            val pattern = "h:mm a"
            val sdf = SimpleDateFormat(pattern, Locale.getDefault())
            return sdf.format(timestamp)
        }
    }


    interface OnItemDeleteListener {
        fun onItemDeleted(position: Int, item: NotificationEntity)
    }
}