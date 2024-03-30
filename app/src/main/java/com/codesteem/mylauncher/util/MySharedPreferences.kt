package com.codesteem.mylauncher.util

import android.content.Context
import android.util.Log
import com.codesteem.mylauncher.models.NotificationsModel
import com.codesteem.mylauncher.models.UserInfo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object MySharedPreferences {

    // SharedPreferences file name
    private const val PREF_NAME = "MyPref"

    // Keys for different data items stored in SharedPreferences
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

    // Keys for storing notification-related data
    private const val KEY_NOTIFICATIONS = "notifications"
    private const val FIVE_TAPS_HINT = "five_taps_hint"
    const val KEY_PRIORITIZED = "key_prioritized"
    const val SEARCH_COPY_HINT = "search_copy_hint"
    const val KEY_IS_PRIORITY_ACTIVE = "key_is_priority_active"
    const val KEY_NOTIFICATIONS_INTERVAL = "key_notifications_interval"
    const val KEY_NOTIFICATION_TIMESTAMP = "key_notification_timestamp"
    const val KEY_IS_SHOWN_COPY_TEXT_HELP = "key_copy_text_help_shown"

    // Functions for getting and saving notification timestamp
    fun getNotificationTimeStamp(context: Context): Long {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getLong(KEY_NOTIFICATION_TIMESTAMP, 0L)
    }

    fun saveNotificationTimeStamp(context: Context, timeStamp: Long) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putLong(KEY_NOTIFICATION_TIMESTAMP, timeStamp).apply()
    }

    // Function for getting and saving the status of the "copy text" help balloon
    fun getCopeTextHelpStatus(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(KEY_IS_SHOWN_COPY_TEXT_HELP, false)
    }

    fun saveCopeTextHelpStatus(context: Context, isShown: Boolean) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean(KEY_IS_SHOWN_COPY_TEXT_HELP, isShown).apply()
    }

    // Functions for getting and saving notification interval
    fun getNotificationInterval(context: Context): Int {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(KEY_NOTIFICATIONS_INTERVAL, 0)
    }

    fun saveNotificationsInterval(context: Context, interval: Int) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putInt(KEY_NOTIFICATIONS_INTERVAL, interval).apply()
    }

    // Function for getting and setting priority mode
    fun getPriorityMode(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(KEY_IS_PRIORITY_ACTIVE, false)
    }

    fun savePriority(isActive: Boolean, context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean(KEY_IS_PRIORITY_ACTIVE, isActive).apply()
    }

    // Functions for managing prioritized apps
    fun isPrioritized(app: String, context: Context): Boolean {
        val currentList = getPrioritized(context).toMutableList()
        return currentList.contains(app)
    }

    fun savePriorities(list: List<String>, context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putStringSet(KEY_PRIORITIZED, list.toSet())
        editor.apply()
    }

    fun removePriority(priority: String, context: Context) {
        val currentList = getPrioritized(context).toMutableList()
        if (currentList.contains(priority)) {
            currentList.remove(priority)
            savePriorities(currentList, context)
        }
    }

    fun savePriority(priority: String, context: Context) {
        val currentList = getPrioritized(context).toMutableList()
        if (!currentList.contains(priority)) {
            currentList.add(priority)
            savePriorities(currentList, context)
        }
    }

    fun getPrioritized(context: Context): List<String> {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getStringSet(KEY_PRIORITIZED, setOf())?.toList() ?: emptyList()
    }

   
