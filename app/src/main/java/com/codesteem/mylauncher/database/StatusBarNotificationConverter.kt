package com.codesteem.mylauncher.database

import android.service.notification.StatusBarNotification
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.io.IOException

object StatusBarNotificationConverter {

    private val gson = GsonBuilder()
        .registerTypeAdapter(RuntimeException::class.java, RuntimeExceptionAdapter())
        .registerTypeAdapter(StatusBarNotification::class.java, ParcelableTypeAdapter(StatusBarNotification.CREATOR))
        .create()

    private object RuntimeExceptionAdapter : TypeAdapter<RuntimeException>() {
        override fun write(out: JsonWriter, value: RuntimeException?) {
            if (value == null) {
                out.nullValue()
                return
            }
            out.value(value.message)
        }

        override fun read(input: JsonReader): RuntimeException? {
            return RuntimeException(input.nextString())
        }
    }

    private class ParcelableTypeAdapter<T : Parcelable>(private
