package com.codesteem.mylauncher.database

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

/**
 * A custom [TypeAdapter] for serializing and deserializing [Parcelable] objects using a Gson [JsonReader] and [JsonWriter].
 * This class is designed to handle the specific case where the [Parcelable] object needs to be created from a JSON object
 * that contains a "CREATOR" field with a nested "createFromParcel" field.
 *
 * Note: This class only supports deserialization (reading), and will throw an [UnsupportedOperationException]
 * if you try to serialize (write) a [Parcelable] object using this adapter.
 */
class ParcelableTypeAdapter<T : Parcelable>(private val creator: Parcelable.Creator<T>) : TypeAdapter<T>() {
    /**
     * Throws an [UnsupportedOperationException] indicating that serialization (writing) is not supported
     * for [Parcelable] types using this adapter.
     */
    override fun write(writer: JsonWriter?, value: T?) {
        throw UnsupportedOperationException("Serialization is not supported for Parcelable types.")
    }

    /**
     * Deserializes a [Parcelable] object from a [JsonReader] by reading the JSON object and using the provided
     * [creator] to instantiate the object from a [Parcel] obtained from the JSON object's "CREATOR" field.
     *
     * @param reader The [JsonReader] to read the JSON object from.
     * @return The deserialized [Parcelable] object, or null if the JSON object is null or malformed.
     */
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
                                    // Use the provided creator to instantiate the Parcelable object from a Parcel
                                    // obtained from the JSON object's "CREATOR" field.
                                    return creator.createFromParcel(Parcel.obtain())
                                } else {
                                    reader.
