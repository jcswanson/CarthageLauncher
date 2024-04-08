package com.codesteem.mylauncher.util

import android.content.Context
import android.util.Log
import com.codesteem.mylauncher.models.NotificationsModel
import com.codesteem.mylauncher.models.UserInfo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Object that handles storing and retrieving data in SharedPreferences.
 */
object MySharedPreferences {

    /**
     * The name of the shared preferences file.
     */
    private const val PREF_NAME = "MyPref"

    // Keys for storing different types of data in shared preferences
    private const val KEY_STRING_LIST = "string_list"
    private const val KEY_STRING_LIST_ALL = "string_list_all"
    const val KEY_USER_IMAGE = "user_image"
    const val KEY_USER_NAME = "user_name"
    const val KEY_PHONE = "phone"
    const val KEY_EMAIL = "email"
    const val KEY_WHATSAPP = "whatsapp"
    const val KEY_INSTAGRAM = "instagram"
    const val KEY_MESSENGER = "messenger"
    const val KEY_SNAPCHAT = "snapchat"
    const val KEY_X = "x"
    const val KEY_DISCORD_NOW = "discord_now"

    private const val KEY_NOTIFICATIONS = "notifications"
    private const val FIVE_TAPS_HINT = "five_taps_hint"
    const val KEY_PRIORITIZED = "key_prioritized"
    const val SEARCH_COPY_HINT = "search_copy_hint"
    const val KEY_IS_PRIORITY_ACTIVE = "key_is_priority_active"
    const val KEY_NOTIFICATIONS_INTERVAL = "key_notifications_interval"
    const val KEY_NOTIFICATION_TIMESTAMP = "key_notification_timestamp"
    const val KEY_IS_SHOWN_COPY_TEXT_HELP = "key_copy_text_help_shown"

    /**
     * Returns the notification timestamp stored in shared preferences.
     */
    fun getNotificationTimeStamp(context: Context): Long {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getLong(KEY_NOTIFICATION_TIMESTAMP, 0L)
    }

    /**
     * Saves the notification timestamp to shared preferences.
     */
    fun saveNotificationTimeStamp(context: Context, timeStamp: Long) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putLong(KEY_NOTIFICATION_TIMESTAMP, timeStamp).apply()
    }

    // ... More functions with comments ...

    /**
     * Returns the user info object stored in shared preferences.
     */
    fun getUserInfo(context: Context): UserInfo? {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return UserInfo(
            sharedPreferences.getString(KEY_USER_IMAGE, ""),
            sharedPreferences.getString(KEY_USER_NAME, ""),
            sharedPreferences.getString(KEY_PHONE, ""),
            sharedPreferences.getString(KEY_EMAIL, ""),
            sharedPreferences.getString(KEY_WHATSAPP, ""),
            sharedPreferences.getString(KEY_INSTAGRAM, ""),
            sharedPreferences.getString(KEY_MESSENGER, ""),
            sharedPreferences.getString(KEY_SNAPCHAT, ""),
            sharedPreferences.getString(KEY_X, ""),
            sharedPreferences.getString(KEY_DISCORD_NOW, "")
        )
    }

    // ... More functions with comments ...
}
