package com.codesteem.mylauncher.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat

object NotificationPermissionHelper {

    const val REQUEST_CODE_NOTIFICATION_PERMISSION = 101

    /**
     * Checks if the notification permission is granted for the app.
     *
     * @param context The context of the app.
     * @return True if the notification permission is granted, false otherwise.
     */
    fun checkNotificationPermission(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            notificationManager.areNotificationsEnabled()
        } else {
            notificationManager.notificationPolicyAccessGranted
        }
    }

    /**
     * Requests notification permission for the app.
     *
     * @param activity The activity context of the app.
     */
    fun requestNotificationPermission(activity: Activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            requestNotificationPolicyAccess(activity)
        } else {
            requestNotificationAccess(activity)
        }
    }

    /**
     * Checks if the notification service is enabled for the app.
     *
     * @param context The context of the app.
     * @return True if the notification service is enabled, false otherwise.
     */
    fun isNotificationServiceEnabled(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT_WATCH) {
            return true
        }

        val contentResolver = context.contentResolver
        val packageName = context.packageName
        val listenerSettings =
            Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
        return listenerSettings?.split(":")?.any { it.endsWith("$packageName") } == true
    }

    private fun requestNotificationPolicyAccess(activity: Activity) {
        try {
            activity.startActivityForResult(
                Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS),
                REQUEST_CODE_NOTIFICATION_PERMISSION
            )
        } catch (e: ActivityNotFoundException) {
            // Handle the exception
        }
    }

    private fun requestNotificationAccess(activity: Activity) {
        if (activity.applicationInfo.targetSdkVersion >= Build.VERSION_CODES.Q) {
            return
        }

        if (NotificationManagerCompat.from(activity).areNotificationsEnabled()) {
            return
        }

        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, activity.packageName)
        activity.startActivityForResult(intent, REQUEST_CODE_NOTIFICATION_PERMISSION)
    }
}
