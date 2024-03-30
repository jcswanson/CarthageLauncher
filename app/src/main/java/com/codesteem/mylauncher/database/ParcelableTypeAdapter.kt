package com.codesteem.mylauncher.database

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class ParcelableTypeAdapter<T : Parcelable>(private val creator: Parcelable.Creator<T>) : TypeAdapter<T>() {

    private val gson = Gson()

    override fun write(writer: JsonWriter?, value: T?) {
        if (value == null) {
            writer?.nullValue()
            return
        }

        val parcel = Parcel.obtain()
        value.writeToParcel(parcel, 0)
        parcel.setDataPosition(0)

        val parcelString = convertParcelToJson(parcel)
        writer?.jsonValue(parcelString)

        parcel.recycle()
    }

    private fun convertParcelToJson(parcel: Parcel): String {
        val parcelable = creator.createFromParcel(parcel)
        return gson.toJson(parcelable)
    }

    override fun read(reader: JsonReader?): T? {
        if (reader == null) {
            return null
        }

        val parcelString = reader.nextString()
        val parcel = convertJsonToParcel(parcelString)

        return creator.createFromParcel(parcel)
    }

    private fun convertJsonToParcel(parcelString: String): Parcel {
        val parcel = Parcel.obtain()
        parcel.writeString(parcelString)
        parcel.setDataPosition(0)

        return parcel
    }
}
