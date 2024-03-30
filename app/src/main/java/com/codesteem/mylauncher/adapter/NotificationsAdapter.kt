package com.codesteem.mylauncher.adapter

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.Nullable
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
import java.util.Date
import java.util.Locale

class NotificationsAdapter(
    private val onItemDeleteListener: OnItemDeleteListener
) : ListAdapter<NotificationEntity, NotificationsAdapter.NotificationsViewHolder>(
    NotificationItemCallBack()
),
    ItemTouchHelperAdapter {

    class NotificationItemCallBack : DiffUtil.ItemCallback<NotificationEntity>() {
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
        return NotificationsViewHolder(binding).also {
            it.onNotificationClickListener = onNotificationClickListener
            it.onActionClickListener = onActionClickListener
        }
    }

    override fun onBindViewHolder(holder: NotificationsViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onItemSwiped(position: Int, direction: Int, viewHolder: RecyclerView.ViewHolder) {
        if (position in 0 until currentList.size) {
            onItemDeleteListener.onItemDeleted(position, currentList[position])
        }
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
    }

    private lateinit var onNotificationClickListener: (notification: NotificationEntity) -> Unit

    fun setOnNotificationClickListener(onNotificationClick: (notification: NotificationEntity) -> Unit) {
        onNotificationClickListener = onNotificationClick
    }

    private lateinit var onActionClickListener: (notification: NotificationEntity) -> Unit

    fun setOnActionClickListener(onActionClick: (notification: NotificationEntity) -> Unit) {
        onActionClickListener = onActionClick
    }

    class NotificationsViewHolder(
        private val binding: ItemNotificationBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        var onNotificationClickListener: ((notification: NotificationEntity) -> Unit)? = null
        var onActionClickListener: ((notification: NotificationEntity) -> Unit)? = null

        @SuppressLint("SetTextI18n")
        fun bind(
            notiModel: NotificationEntity
        ) {
            with(itemView) {
                binding.replyEt.text.clear()
                binding.tvName.text = notiModel.title
                binding.tvType.text = notiModel.text
                binding.tvTime.text = "• ${notiModel.appInfo?.appName} • ${formatTimeFromTimestamp(notiModel.timeStamp!!)}"
                Glide.with(context).load(notiModel.appInfo?.appIconByteArray).into(binding.ivAppIcon)
                binding.linAlarmTime.visibility = View.GONE
                binding.tvTime.visibility = View.VISIBLE

                val actions = notiModel.actions
                binding.bottomLinearLayout.removeAllViews()

                when {
                    actions.find { it.equals("reply", true) || it.equals("quick reply", true) } != null -> {
                        binding.replyLayout.visibility = View.VISIBLE
                    }
                    else -> {
                        binding.replyLayout.visibility = View.GONE
                    }
                }

                setupActions(
                    notiModel,
                    notiModel.actions,
                    binding.bottomLinearLayout,
                    onActionClickListener
                )
                setupReply(
                    notiModel,
                    binding.replyLayout,
                    binding.replyEt,
                    binding.replyBtn,
                    onActionClickListener
                )
                setOnClickListener {
                    noti
