package com.codesteem.mylauncher.database

import androidx.room.TypeConverter
import com.codesteem.mylauncher.models.AppInfo
import com.google.gson.Gson

/**
 * Converts AppInfo objects to and from strings, allowing them to be stored in the Room database.
 */
class AppInfoConverter {

    /**
     * Converts an AppInfo object to a JSON string, which can be stored in the Room database.
     *
     * @param appInfo The AppInfo object to convert.
     * @return A JSON string representing the AppInfo object, or null if the input was null.
     */
    @TypeConverter
    fun fromAppInfo(appInfo: AppInfo?): String? {
        return Gson().toJson(appInfo)
    }

    /**
     * Converts a JSON string to an AppInfo object, which can be used in the app.
     *
     * @param value The JSON string to convert.
     * @return An AppInfo object representing the JSON data, or null if the input was null or invalid.
     */
    @TypeConverter
    fun toAppInfo(value: String?): AppInfo? {
        return Gson().fromJson(value, AppInfo::class.java)
    }
}

