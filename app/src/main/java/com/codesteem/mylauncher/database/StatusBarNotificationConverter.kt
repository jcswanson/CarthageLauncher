package com.codesteem.mylauncher.database

import android.service.notification.StatusBarNotification
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.lang.reflect.Modifier

class StatusBarNotificationConverter {

    val gson = GsonBuilder()
        .excludeFieldsWithModifiers()
        .registerTypeAdapter(RuntimeException::class.java, RuntimeExceptionAdapter())
        .registerTypeAdapter(StatusBarNotification::class.java, ParcelableTypeAdapter(StatusBarNotification.CREATOR))
        .create()

    @TypeConverter
    fun fromStatusBarNotification(statusBarNotification: StatusBarNotification?): String? {
        return gson.toJson(statusBarNotification)
    }

    @TypeConverter
    fun toStatusBarNotification(value: String?): StatusBarNotification? {
        return gson.fromJson(value, StatusBarNotification::class.java)
    }
}
