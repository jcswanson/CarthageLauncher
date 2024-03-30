package com.codesteem.mylauncher.database;

import androidx.room.TypeConverter;

import java.nio.charset.StandardCharsets;

/**
 * A class that provides type conversion methods for storing byte arrays as strings in the Room database.
 * This is necessary because Room does not support storing byte arrays directly.
 */
class ByteArrayConverter {

    /**
     * Converts a byte array to a string for storage in the Room database.
     *
     * @param byteArray The byte array to convert.
     * @return A string representation of the byte array, or an empty string if the input is null.
     */
    @TypeConverter
    public fun fromByteArray(byteArray: ByteArray?): String {
        return byteArray?.let { String(it, StandardCharsets.UTF_8) } ?: ""
    }

    /**
     * Converts a string to a byte array for retrieval from the Room database.
     *
     * @param value The string to convert.
     * @return A byte array representation of the string, or null if the input is an empty string.
     */
    @TypeConverter
    public fun toByteArray(value: String?): ByteArray? {
        return value?.let { value.toByteArray(StandardCharsets.UTF_8) }
    }
}
