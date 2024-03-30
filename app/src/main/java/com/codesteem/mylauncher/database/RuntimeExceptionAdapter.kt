import com.google.gson.*
import java.lang.RuntimeException

class RuntimeExceptionAdapter : JsonSerializer<RuntimeException>, JsonDeserializer<RuntimeException> {

    override fun serialize(runtimeException: RuntimeException, type: Type, jsonSerializationContext: JsonSerializationContext): JsonElement {
        return jsonSerializationContext.serialize(runtimeException.message)
    }

    override fun deserialize(jsonElement: JsonElement, type: Type, jsonDeserializationContext: JsonDeserializationContext): RuntimeException {
        val message = jsonDeserializationContext.deserialize<String>(jsonElement, String::class.java)
        return RuntimeException(message)
    }
}
