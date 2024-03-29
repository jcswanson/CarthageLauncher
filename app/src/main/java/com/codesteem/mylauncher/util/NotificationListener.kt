package com.codesteem.mylauncher.util

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
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
import android.os.Bundle
import android.provider.Telephony
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Base64
import android.util.Log
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
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Locale


class NotificationListener : NotificationListenerService() {

    lateinit var myNotifications: ArrayList<StatusBarNotification>

    private val notificationDao by lazy {
        NotificationDatabase.getDatabase(applicationContext).notificationDao()
    }

    private val notificationJob = SupervisorJob()
    private val notificationScope = CoroutineScope(Dispatchers.IO + notificationJob)

    companion object {
        var isRunning = false
        var instance: NotificationListener? = null
    }

    private var currentAction: Notification.Action? = null

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

    private fun getOnlyPrioritized(): List<StatusBarNotification> {
        val notifications = mutableListOf<StatusBarNotification>()
        activeNotifications.forEach { noti ->
            if (MySharedPreferences.isPrioritized(noti.packageName, this)) {
                if (!isSystemApp(packageName) && noti.notification.flags == Notification.FLAG_GROUP_SUMMARY) {
                    notifications.add(noti)
                }
            }
        }
        return notifications
    }

    fun getNotifications(): List<StatusBarNotification> {
        val notifications = mutableListOf<StatusBarNotification>()
        if (MySharedPreferences.getPriorityMode(this)) {
            return getOnlyPrioritized()
        }
        activeNotifications.forEach {
            val packageName = it.packageName
            if (!isSystemApp(packageName) && it.notification.flags == Notification.FLAG_GROUP_SUMMARY) {
                notifications.add(it)
            }
            //Log.e("group", "${it.packageName}: ${it.notification.group}")
            //Log.e("isGroup", "${it.packageName}: ${it.isGroup}")
        }
        return notifications.toList()
    }

    fun reply(message: String, exception: Exception.() -> Unit = {}) {
        try {
            if (currentAction != null) {
                val sendIntent = Intent()
                val msg = Bundle()
                for (input in currentAction!!.remoteInputs) {
                    msg.putCharSequence(input.resultKey, message)
                }
                RemoteInput.addResultsToIntent(currentAction!!.remoteInputs, sendIntent, msg)
                currentAction!!.actionIntent.send(applicationContext, 0, sendIntent)
            }

        } catch (e: Exception) {
            Log.e("exception", exception.toString())
        }
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        // Process the incoming notification here
            try {
                if (sbn != null) {
                    currentAction = sbn.notification.actions.find { it.title == "Reply"  || it.title.toString().equals("quick reply", true)}
                    val packageName = sbn.packageName
                    Log.e("packageName", packageName)
                    cancelNotification(sbn.key)
                    cancelGroupNotifications(sbn.groupKey)
                    cancelAllNotifications()
                    val defaultSmsApp = Telephony.Sms.getDefaultSmsPackage(this)
                    if (isSystemApp(packageName)
                        && packageName != "com.google.android.apps.messaging"
                        && packageName != "com.google.android.dialer"
                        ) {
                        return
                    }
                    val notification = sbn.notification
                    val title = notification.extras.getString(Notification.EXTRA_TITLE)
                    val text = notification.extras.getString(Notification.EXTRA_TEXT)
                    val isReplied = title?.endsWith(": you", true) == true && text?.endsWith(applicationContext.getString(R.string.icon_replied), true) == true
                    if (sbn.notification.flags == Notification.FLAG_GROUP_SUMMARY || isReplied) return
                    sbn.notification.actions.forEach {
                        //Log.e("action", it.title.toString())
                        //Log.e("action", it.title.toString())
                    }
                    //Log.e("count", sbn.notification.number.toString())
                    //Log.e("noti", "${sbn.packageName}: ${sbn.notification.toString()}")
                    //Log.e("flag", Notification.FLAG_GROUP_SUMMARY.toString())
                    //Log.e("groupBehavior", "${sbn.packageName}: ${sbn.notification.flags}")
                    sendMessageToActivity(true)
                    //saveNotification(this, getNotiData(sbn))
                    //Log.e("get", getNotifications(this).size.toString())
                    //cancelNotification(sbn.key)
                    // Filter out badge notifications based on their properties


                    val notificationTime = sbn.postTime
                    val smallIconBitmap: Bitmap? = sbn.notification.smallIcon?.loadDrawable(this)?.toBitmap()
                    val smallIconByteArray = smallIconBitmap?.let { bitmapToByteArray(it) }
                    val appInfo: AppInfo? = getApplicationInfo(applicationContext, packageName)
                    if (title != null) myNotifications.add(sbn)
                    val listOfActions = mutableListOf<String>()
                    sbn.notification.actions.forEach {
                        listOfActions.add(it.title.toString())
                    }
                    notificationScope.launch {
                        try {
                            notificationDao
                                .insertNotification(
                                    NotificationEntity(
                                        id = sbn.key,
                                        title = title,
                                        text = text,
                                        timeStamp = notificationTime,
                                        appInfo = appInfo,
                                        listOfActions,
                                        isPrioritized =  MySharedPreferences.isPrioritized(sbn.packageName,this@NotificationListener)
                                    )
                                )
                            sendNotificationBroadcast(sbn)
                        }catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    if (canShowNotification()) {

                    }

                }

            }catch (ex:Exception){}
    }

    private fun canShowNotification(): Boolean {
        val now = System.currentTimeMillis()
        val lastNotificationTime = MySharedPreferences.getNotificationTimeStamp(this)
        val interval = MySharedPreferences.getNotificationInterval(this)
        val convertedInterval = minutesToMilliseconds(interval)

        // Check if enough time has passed since the last notification
        return now - lastNotificationTime >= convertedInterval
    }

    private fun minutesToMilliseconds(minutes: Int): Long {
        val millisecondsInMinute = 60 * 1000L // 60 seconds * 1000 milliseconds
        return minutes * millisecondsInMinute
    }

    private fun cancelGroupNotifications(groupKey: String) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val activeNotifications = notificationManager.activeNotifications

        for (notification in activeNotifications) {
            if (notification.notification.group == groupKey) {
                // Cancel the notification by ID
                notificationManager.cancel(notification.id)
            }
        }
    }


    override fun onNotificationRemoved(
        sbn: StatusBarNotification?,
        rankingMap: RankingMap?,
        reason: Int
    ) {
        super.onNotificationRemoved(sbn, rankingMap, reason)
        if (sbn != null) {
            /*notificationScope.launch {
                Log.e("get", notificationDao.getNotificationById(sbn.key)?.id.toString())
                notificationDao.deleteNotificationById(sbn.key)
            }*/
            //sendMessageToActivity(true)
            //removeNotification(this, getNotiData(sbn))
            cancelNotification(sbn.key)
        }
        Log.e("noti", "removed")
    }

    private fun isAppDefaultLauncher(): Boolean {
        val packageName = packageName
        val componentName = ComponentName(packageName, "$packageName.MainActivity") // Replace with your main activity

        val pm = packageManager
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        val resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)

        return resolveInfo?.activityInfo?.name == componentName.className
    }

    private fun sendMessageToActivity(msg: Boolean) {
        val intent = Intent("NOTI")
        // You can also include some extra data.
        intent.putExtra("Status", msg)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    override fun getActiveNotifications(keys: Array<out String>?): Array<StatusBarNotification> {
        return super.getActiveNotifications(keys)
    }

    private fun isSystemApp(packageName: String): Boolean {
        try {
            val packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA)
            return packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
        } catch (e: PackageManager.NameNotFoundException) {
            // Handle the exception if the package is not found
            return false
        }
    }

    private fun getNotiData(sbn: StatusBarNotification): NotificationsModel {
        val notification = sbn.notification

        val title = notification.extras.getString(Notification.EXTRA_TITLE)
        val text = notification.extras.getString(Notification.EXTRA_TEXT)

        val notificationTime = sbn.postTime // Notification timestamp in milliseconds

        // Get the notification icon (small icon)
        val smallIconBitmap: Bitmap? = sbn.notification.smallIcon?.loadDrawable(this)?.toBitmap()
        val smallIconByteArray = smallIconBitmap?.let { bitmapToByteArray(it) }

        // You can also get the large icon (if available)
        val packageName = sbn.packageName
        val appInfo: AppInfo? = getApplicationInfo(applicationContext, packageName)
        val appIcon: Drawable = byteArrayToDrawable(appInfo!!.appIconByteArray)
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

    private fun byteArrayToDrawable(byteArray: ByteArray): Drawable {
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        return BitmapDrawable(resources, bitmap)
    }

    fun formatTimeFromTimestamp(timestamp: Long): String {
        val pattern = "h:mm a"
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        return sdf.format(timestamp)
    }

    private fun refresh() {
        val intent = Intent("REFRESH")
        sendBroadcast(intent)
    }

    private fun sendNotificationBroadcast(sbn: StatusBarNotification) {
        // Send the notification details via broadcast
        val notification = sbn.notification

        val id = sbn.key
        val title = notification.extras.getString(Notification.EXTRA_TITLE)
        val text = notification.extras.getString(Notification.EXTRA_TEXT)

        val notificationTime = sbn.postTime // Notification timestamp in milliseconds

        // Get the notification icon (small icon)
        val smallIconBitmap: Bitmap? = sbn.notification.smallIcon?.loadDrawable(this)?.toBitmap()
        val smallIconByteArray = smallIconBitmap?.let { bitmapToByteArray(it) }

        // You can also get the large icon (if available)
        val packageName = sbn.packageName
        val appInfo: AppInfo? = getApplicationInfo(applicationContext, packageName)

        val actions = sbn.notification.actions ?: emptyArray()

        // Send all notification details via broadcast
        val intent = Intent("ACTION_NOTIFICATION_RECEIVED")
        intent.putExtra("id", id)
        intent.putExtra("title", title)
        intent.putExtra("text", text)
        intent.putExtra("pkg", packageName)
        intent.putExtra("app_info", appInfo)
        intent.putExtra("heads-up", isHeadsUpNotification(notification))
        intent.putExtra("notificationTime", notificationTime)
        intent.putExtra("smallIcon", smallIconByteArray)
        intent.putExtra("notiKey", sbn.key)
        intent.putExtra("actions", actions)

        if (sbn.notification.actions.find { it.title == "Reply" } != null) {
            intent.putExtra("replyAction", true)
        } else {
            intent.putExtra("replyAction", false)
        }

        sendBroadcast(intent)
    }

    // Check if the notification is a heads-up notification
    fun isHeadsUpNotification(notification: Notification): Boolean {
        // Check if the priority is set to PRIORITY_HIGH or PRIORITY_MAX
        return (notification.priority == NotificationCompat.PRIORITY_HIGH ||
                notification.priority == NotificationCompat.PRIORITY_MAX)
    }
    // Utility function to convert Bitmap to a byte array
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
}


