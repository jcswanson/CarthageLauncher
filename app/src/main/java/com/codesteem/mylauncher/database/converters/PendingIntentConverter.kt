package com.codesteem.mylauncher.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * A class that provides type converters for converting a list of strings to and from a single string,
 * for use with Room database.
 */
class StringListConverter {

    /**
     * Converts a list of strings to a single string using Gson.
     *
     * @param list The list of strings to convert.
     * @return The string representation of the list.
     */
    @TypeConverter
    fun fromList(list: List<String>?): String {
        return Gson().toJson(list ?: emptyList<String>())
    }

    /**
     * Converts a single string to a list of strings using Gson.
     *
     * @param value The string representation of the list to convert.
     * @return The list of strings, or an empty list if the string is null or empty.
     */
    @TypeConverter
    fun fromString(value: String?): List<String>? {
        if (value == null) {
            return emptyList()
        }

        // Use a TypeToken to specify the type of the list being converted.
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }
}

