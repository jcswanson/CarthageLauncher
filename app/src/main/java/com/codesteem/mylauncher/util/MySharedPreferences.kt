package com.codesteem.mylauncher.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.codesteem.mylauncher.models.NotificationsModel
import com.codesteem.mylauncher.models.UserInfo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object MySharedPreferences {

    private const val PREF_NAME = "MyPref"
    private const val NOTIFICATIONS_KEY = "notifications"
    private const val USER_INFO_KEY = "user_info"
    private const val PRIORITIZED_APPS_KEY = "prioritized_apps"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    private fun getGson(): Gson {
        return Gson()
    }

    fun saveNotificationTimeStamp(context: Context, timeStamp: Long) {
        getSharedPreferences(context).edit().putLong(NOTIFICATIONS_KEY, timeStamp).apply()
    }

    fun getNotificationTimeStamp(context: Context): Long {
        return getSharedPreferences(context).getLong(NOTIFICATIONS_KEY, 0L)
    }

    fun saveUserInfo(context: Context, userInfo: UserInfo) {
        val gson = getGson()
        val json = gson.toJson(userInfo)
        getSharedPreferences(context).edit().putString(USER_INFO_KEY, json).apply()
    }

    fun getUserInfo(context: Context): UserInfo? {
        val gson = getGson()
        val json = getSharedPreferences(context).getString(USER_INFO_KEY, null)
        if (json == null) {
            return null
        }
        return gson.fromJson(json, UserInfo::class.java)
    }

    fun savePrioritizedApps(context: Context, apps: List<String>) {
        val gson = getGson()
        val json = gson.toJson(apps)
        getSharedPreferences(context).edit().putString(PRIORITIZED_APPS_KEY, json).apply()
    }

    fun getPrioritizedApps(context: Context): List<String> {
        val gson = getGson()
        val json = getSharedPreferences(context).getString(PRIORITIZED_APPS_KEY, "[]")
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(json, type)
    }

    fun isPrioritized(app: String, context: Context): Boolean {
        return getPrioritizedApps(context).contains(app)
    }

    fun savePriority(app: String, context: Context, isActive: Boolean) {
        val currentList = getPrioritizedApps(context)
        if (isActive) {
            if (!currentList.contains(app)) {
                currentList.add(app)
                savePrioritizedApps(context, currentList)
            }
        } else {
            if (currentList.contains(app)) {
                currentList.remove(app)
                savePrioritizedApps(context, currentList)
            }
        }
    }
}
