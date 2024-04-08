// This class serves as a custom adapter for serializing and deserializing ColorStateList objects
// using Google's Gson library.
class ColorStateListAdapter : JsonSerializer<ColorStateList>, JsonDeserializer<ColorStateList> {

    // The serialize method converts a ColorStateList object into a JsonElement object that can
    // be used for serialization by Gson. This method takes a ColorStateList object, a Type object
    // (which is not used in this implementation), and a JsonSerializationContext object as its
    // parameters.
    override fun serialize(src: ColorStateList?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        // If the JsonSerializationContext object is null, an IllegalArgumentException is thrown.
        return context?.serialize(src)
            ?: throw IllegalArgumentException("Unable to serialize ColorStateList without JsonSerializationContext")
    }

    // The deserialize method converts a JsonElement object into a ColorStateList object that can
    // be used for deserialization by Gson. This method takes a JsonElement object, a Type object
    // (which is not used in this implementation), and a JsonDeserializationContext object as its
    // parameters.
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): ColorStateList {
        // If the JsonDeserializationContext object is null, an IllegalArgumentException is thrown.
        return context?.deserialize(json, typeOfT)
            ?: throw IllegalArgumentException("Unable to deserialize ColorStateList without JsonDeserializationContext")
    }
}
