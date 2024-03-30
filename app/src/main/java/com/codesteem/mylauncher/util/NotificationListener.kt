package com.codesteem.mylauncher.util

import android.app.KeyguardManager
import android.app.Notification
import android.app.NotificationManager
import android.app.RemoteInput
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.PowerManager
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.text.TextUtils
import android.util.Base64
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.codesteem.mylauncher.R
import com.codesteem.mylauncher.database.NotificationDatabase
import com.codesteem.mylauncher.database.NotificationEntity
import com.codesteem.mylauncher.models.AppInfo
import com.codesteem.mylauncher.models.NotificationsModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.locks.ReentrantLock

class NotificationListener : NotificationListenerService() {

    private lateinit var myNotifications: ArrayList<StatusBarNotification>
    private val notificationDao by lazy {
        NotificationDatabase.getDatabase(applicationContext).notificationDao()
    }
    private val notificationJob = SupervisorJob()
    private val notificationScope = CoroutineScope(Dispatchers.IO + notificationJob)
    private val actions = HashMap<String, Notification.Action?>()

    companion object {
        var isRunning = false
        var instance: NotificationListener? = null
        const val MSG_ID = "MSG_ID"
    }

    override fun onCreate() {
        super.onCreate()
        myNotifications = ArrayList()
        instance = this
        isRunning = true
        sendMessageToActivity(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        instance = null
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        if (sbn != null) {
            handleNotification(sbn)
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?, rankingMap: RankingMap?, reason: Int) {
        super.onNotificationRemoved(sbn, rankingMap, reason)
        if (sbn != null) {
            notificationScope.launch {
                notificationDao.deleteNotificationById(sbn.key)
            }
            cancelNotification(sbn.key)
        }
    }

    private fun handleNotification(sbn: StatusBarNotification) {
        actions[sbn.notification.shortcutId] = sbn.notification.actions.find { it.title == "Reply" }
        val packageName = sbn.packageName
        if (isSystemApp(packageName) && packageName != "com.google.android.apps.messaging" && packageName != "com.google.android.dialer") {
            return
        }

        val notification = sbn.notification
        val title = notification.extras.getString(Notification.EXTRA_TITLE)
        val text = notification.extras.getString(Notification.EXTRA_TEXT)
        val isReplied = if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(text)) {
            title.endsWith(": you", true) && text.endsWith(applicationContext.getString(R.string.icon_replied), true)
        } else {
            false
        }

        if (notification.flags == Notification.FLAG_GROUP_SUMMARY || isReplied) {
            return
        }

        sendMessageToActivity(true)

        val notificationTime = sbn.postTime
        val smallIconBitmap: Bitmap? = sbn.notification.smallIcon?.loadDrawable(this)?.toBitmap()
        val smallIconByteArray = smallIconBitmap?.let { bitmapToByteArray(it) }
        val appInfo: AppInfo? = getApplicationInfo(applicationContext, packageName)

        if (title != null) {
            myNotifications.add(sbn)
        }

        val listOfActions = mutableListOf<String>()
        sbn.notification.actions.forEach {
            listOfActions.add(it.title.toString())
        }

        notificationScope.launch {
            try {
                notificationDao.insertNotification(
                    NotificationEntity(
                        id = sbn.key,
                        title = title,
                        text = text,
                        timeStamp = notificationTime,
                        appInfo = appInfo,
                        listOfActions,
                        isPrioritized = MySharedPreferences.isPrioritized(sbn.packageName, this@NotificationListener)
                    )
                )
                sendNotificationBroadcast(sbn)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // ... Rest of the code remains the same
}
