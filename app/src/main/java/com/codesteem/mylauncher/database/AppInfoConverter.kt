package com.codesteem.mylauncher.database

import androidx.room.TypeConverter
import com.codesteem.mylauncher.models.AppInfo
import com.google.gson.Gson

class AppInfoConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromAppInfo(appInfo: AppInfo?): String? {
        return gson.toJson(appInfo)
    }

    @TypeConverter
    fun toAppInfo(value: String?): AppInfo? {
        return gson.fromJson(value, AppInfo::class.java)
    }
}
