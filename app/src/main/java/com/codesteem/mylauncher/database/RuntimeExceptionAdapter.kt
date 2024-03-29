package com.codesteem.mylauncher.database

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class RuntimeExceptionAdapter : JsonSerializer<RuntimeException>,
    JsonDeserializer<RuntimeException> {

    override fun serialize(
        src: RuntimeException?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return context?.serialize(src)
            ?: throw IllegalArgumentException("Unable to serialize RuntimeException without JsonSerializationContext")
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): RuntimeException {
        return context?.deserialize(json, typeOfT)
            ?: throw IllegalArgumentException("Unable to deserialize RuntimeException without JsonDeserializationContext")
    }
}
