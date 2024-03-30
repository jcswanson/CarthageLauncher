/**
 * A custom JSON serializer/deserializer for [RuntimeException] instances.
 * This adapter is used to serialize and deserialize RuntimeException objects
 * when converting data to and from JSON format using the Gson library.
 */
class RuntimeExceptionAdapter : JsonSerializer<RuntimeException>, JsonDeserializer<RuntimeException> {

    /**

