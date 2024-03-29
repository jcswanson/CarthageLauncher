package com.codesteem.mylauncher.util

import android.content.Context
import android.util.Log
import com.codesteem.mylauncher.models.NotificationsModel
import com.codesteem.mylauncher.models.UserInfo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


object MySharedPreferences {
    private const val PREF_NAME = "MyPref"
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
    private const val KEY_PRIORITIZED = "key_prioritized"
    private const val SEARCH_COPY_HINT = "search_copy_hint"
    const val KEY_IS_PRIORITY_ACTIVE = "key_is_priority_active"
    const val KEY_NOTIFICATIONS_INTERVAL = "key_notifications_interval"
    const val KEY_NOTIFICATION_TIMESTAMP = "key_notification_timestamp"
    const val KEY_IS_SHOWN_COPY_TEXT_HELP = "key_copy_text_help_shown"

    fun getNotificationTimeStamp(context: Context): Long {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getLong(KEY_NOTIFICATION_TIMESTAMP, 0L)
    }

    fun saveNotificationTimeStamp(context: Context, timeStamp: Long) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putLong(KEY_NOTIFICATION_TIMESTAMP, timeStamp).apply()
    }

    fun getCopeTextHelpStatus(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(KEY_IS_SHOWN_COPY_TEXT_HELP, false)
    }

    fun saveCopeTextHelpStatus(context: Context, isShown: Boolean) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean(KEY_IS_SHOWN_COPY_TEXT_HELP, isShown).apply()
    }

    fun getNotificationInterval(context: Context): Int {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(KEY_NOTIFICATIONS_INTERVAL, 0)
    }

    fun saveNotificationsInterval(context: Context, interval: Int) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putInt(KEY_NOTIFICATIONS_INTERVAL, interval).apply()
    }

    fun getPriorityMode(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(KEY_IS_PRIORITY_ACTIVE, false)
    }


    fun savePriority(isActive: Boolean, context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean(KEY_IS_PRIORITY_ACTIVE, isActive).apply()
    }

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

    fun saveSearchCopyBallon(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean(SEARCH_COPY_HINT, true).apply()
    }

    fun isSearchCopBallonShown(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(SEARCH_COPY_HINT, false)
    }

    fun saveAppsShownBallon(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean(FIVE_TAPS_HINT, true).apply()
    }

    fun isAppsBallonShown(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(FIVE_TAPS_HINT, false)
    }

    fun getNotifications(context: Context): List<NotificationsModel> {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString(KEY_NOTIFICATIONS, null)
        return if (json != null) {
            val type = object : TypeToken<List<NotificationsModel>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }

    fun saveStringList(context: Context, stringList: List<String?>?) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(stringList)
        editor.putString(KEY_STRING_LIST, json)
        editor.apply()
    }

    fun saveUserInfo(context: Context, userInfo: UserInfo) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(KEY_USER_IMAGE, userInfo.getUserImage())
        editor.putString(KEY_USER_NAME, userInfo.getUserName())
        editor.putString(KEY_PHONE, userInfo.getPhone())
        editor.putString(KEY_EMAIL, userInfo.getEmail())
        editor.putString(KEY_WHATSAPP, userInfo.getWhatsApp())
        editor.putString(KEY_INSTAGRAM, userInfo.getInstagram())
        editor.putString(KEY_MESSENGER, userInfo.getMessenger())
        editor.putString(KEY_SNAPCHAT, userInfo.getSnapchat())
        editor.putString(KEY_X, userInfo.getX())
        editor.putString(KEY_DISCORD_NOW, userInfo.getDiscordNow())
        editor.apply()
    }

    fun saveDataItem(key: String, data: String, context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(key, data).apply()
    }

    fun getDataItem(key: String, context: Context): String {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(key, "") ?: ""
    }

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

    fun convertStringListToPairList(stringList: List<String>, defaultIntValue: Int): List<Pair<String, Int>> {
        return stringList.map { stringItem ->
            stringItem to defaultIntValue
        }
    }
    fun getStringList(context: Context): List<String> {
        return try {
            val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val json = sharedPreferences.getString(KEY_STRING_LIST, null)
            val gson = Gson()
            val type = object : TypeToken<List<String?>?>() {}.type
            gson.fromJson(json, type)
        }catch (ex:Exception){
            emptyList()
        }
    }
    fun getStringListAll(context: Context): List<Pair<String, Int>> {
        return try {
            val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val json = sharedPreferences.getString(KEY_STRING_LIST_ALL, null)
            val gson = Gson()
            val type = object : TypeToken<List<Pair<String, Int>>?>() {}.type
            val stringList = gson.fromJson<List<Pair<String, Int>>>(json, type) ?: emptyList()
            stringList.forEach {
                //Log.e("getApp", "${it.first}: ${it.second}")
            }
            stringList
            // Convert the list of strings to a list of pairs with a default integer value
            //convertStringListToPairList(stringList, 0) // Change 42 to your desired default integer value
        } catch (ex: Exception) {
            emptyList()
        }
    }

    fun modifyPair(context: Context, app: String) {
        val afterModification = incrementIntValueInPairList(context, app)
        saveStringListAll(context, afterModification)
    }

    private fun incrementIntValueInPairList(context: Context, targetString: String): List<Pair<String, Int>> {
        return getStringListAll(context).map { pair ->
            if (pair.first == targetString) {
                // If the string matches the target, increment the integer value
                pair.copy(second = pair.second + 1)
            } else {
                pair
            }
        }
    }

    fun saveStringListAll(context: Context, pairList: List<Pair<String, Int>>?) {
        pairList?.forEach {
            Log.e("save", "${it.first}: ${it.second}")
        }
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(pairList)
        editor.putString(KEY_STRING_LIST_ALL, json)
        editor.apply()
    }
}
