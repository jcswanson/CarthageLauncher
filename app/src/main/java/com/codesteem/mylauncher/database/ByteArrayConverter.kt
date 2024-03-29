package com.codesteem.mylauncher.database

import androidx.room.TypeConverter

class ByteArrayConverter {

    @TypeConverter
    fun fromByteArray(byteArray: ByteArray?): String? {
        return byteArray?.let { String(it) }
    }

    @TypeConverter
    fun toByteArray(value: String?): ByteArray? {
        return value?.toByteArray()
    }
}
