package com.codesteem.mylauncher.database

import androidx.room.TypeConverter
import com.codesteem.mylauncher.models.AppInfo
import com.google.gson.Gson

class AppInfoConverter {

    @TypeConverter
    fun fromAppInfo(appInfo: AppInfo?): String? {
        return Gson().toJson(appInfo)
    }

    @TypeConverter
    fun toAppInfo(value: String?): AppInfo? {
        return Gson().fromJson(value, AppInfo::class.java)
    }
}
