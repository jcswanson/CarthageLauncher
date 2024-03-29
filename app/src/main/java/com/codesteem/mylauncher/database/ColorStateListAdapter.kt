package com.codesteem.mylauncher.database

import android.content.res.ColorStateList
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class ColorStateListAdapter : JsonSerializer<ColorStateList>, JsonDeserializer<ColorStateList> {

    override fun serialize(src: ColorStateList?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return context?.serialize(src)
            ?: throw IllegalArgumentException("Unable to serialize ColorStateList without JsonSerializationContext")
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): ColorStateList {
        return context?.deserialize(json, typeOfT)
            ?: throw IllegalArgumentException("Unable to deserialize ColorStateList without JsonDeserializationContext")
    }
}
