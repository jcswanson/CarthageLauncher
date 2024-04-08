// This class, `RuntimeExceptionAdapter`, is a custom JSON serializer and deserializer for the `RuntimeException` class.
// It allows us to convert `RuntimeException` objects to and from JSON format using the Google Gson library.

class RuntimeExceptionAdapter : JsonSerializer<RuntimeException>,
    JsonDeserializer<RuntimeException> {

    // The `serialize` method converts a `RuntimeException` object to a JSON element.
    // It takes three parameters: the `RuntimeException` object to serialize, the type of the object,
    // and a `JsonSerializationContext` object that provides access to serialization services.
    override fun serialize(
        src: RuntimeException?,
        typeOfSrc: Type?,

