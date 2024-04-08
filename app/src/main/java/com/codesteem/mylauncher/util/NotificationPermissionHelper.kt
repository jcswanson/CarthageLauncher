package com.codesteem.mylauncher.util

import android.Manifest
import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat

object NotificationPermissionHelper {

    /**
     * Checks if the notification permission is granted for the app.
     *
     * @param context The context of the app.
     * @return True if the notification permission is granted, false otherwise.
     */
    fun checkNotificationPermission(context: Context): Boolean {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Check if the device is running Android M or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Return true if the app has notification policy access
            return notificationManager.isNotificationPolicyAccessGranted
        }
        // For devices below Android M, assume permission is granted.
        return true
    }

    /**
     * Requests notification permission for the app.
     *
     * @param activity The activity context of the app.
     */
    fun requestNotificationPermission(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Start the activity to grant notification policy access
            val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
            activity.startActivity(intent)
        }
    }

    /**
     * Checks if the notification service is enabled for the app.
     *
     * @param context The context of the app.
     * @return True if the notification service is enabled, false otherwise.
     */
    fun isNotificationServiceEnabled(context: Context): Boolean {
        val services = Settings.Secure.getString(
            context.contentResolver,
            "enabled_notification_listeners"
        )
        // Check if the app's package name is in the list of enabled notification listeners
        return services?.contains(context.packageName) == true
