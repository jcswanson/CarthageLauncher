package com.codesteem.mylauncher.models

import android.graphics.Bitmap

/**
 * Data class representing a notification model with various properties.
 */
data class NotificationsModel(

    /**
     * The time at which the notification was received.
     */
    val time: String? = "",

    /**
     * The URL or file path of the user's image.
     */
    val userImage: String? = "",

    /**
     * The title of the notification.
     */
    val title: String? = "",

    /**
     * The description or body of the notification.
     */
    val desc: String? = "",

    /**
     * The media image associated with the notification.
     */
    val mediaImage: Bitmap? = null,

    /**
     * The type of notification.
     */
    val notificationType: String? = "",

    /**
     * The alarm time associated with the notification.
     */
    val alarmTime: String? = "",

    /**
     * The package name of the related app.
     */
    val relatedAppPackageName: String? = "",

    /**
     * The name of the related app.
     */
    val app_name: String? = "",

    /**
     * The file path or URL of the app icon.
     */
    val app_icon: String? = null,

    /**
     * A unique key for the notification.
     */
    val notiKey: String? = null
)
