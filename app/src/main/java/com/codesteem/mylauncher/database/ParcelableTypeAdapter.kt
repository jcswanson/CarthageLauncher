package com.codesteem.mylauncher.database

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class ParcelableTypeAdapter<T : Parcelable>(private val creator: Parcelable.Creator<T>) : TypeAdapter<T>() {
    override fun write(writer: JsonWriter?, value: T?) {
        throw UnsupportedOperationException("Serialization is not supported for Parcelable types.")
    }

    override fun read(reader: JsonReader?): T? {
        if (reader != null) {
            reader.beginObject()
            while (reader.hasNext()) {
                if (reader.nextName() == "CREATOR") {
                    reader.beginObject()
                    while (reader.hasNext()) {
                        if (reader.nextName() == "createFromParcel") {
                            reader.beginObject()
                            while (reader.hasNext()) {
                                if (reader.nextName() == "creator") {
                                    return creator.createFromParcel(Parcel.obtain())
                                } else {
                                    reader.skipValue()
                                }
                            }
                            reader.endObject()
                        } else {
                            reader.skipValue()
                        }
                    }
                    reader.endObject()
                } else {
                    reader.skipValue()
                }
            }
            reader.endObject()
        }
        return null
    }
}
