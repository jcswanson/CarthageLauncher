// This class serves as a custom adapter for serializing and deserializing ColorStateList objects
// using Google's Gson library.
class ColorStateListAdapter : JsonSerializer<ColorStateList>, JsonDeserializer<ColorStateList> {

    // The serialize method converts a ColorStateList object into a JsonElement object, which
    // can be used to serialize the object as a JSON string. This method takes a ColorStateList
    // object as input, along with its corresponding type and a JsonSerializationContext object.
    // If the context object is null, the method throws an IllegalArgumentException.
    override fun serialize(src: ColorStateList?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        // The serialize method uses the context object to serialize the ColorStateList object
        // and return it as a JsonElement object.
        return context?.serialize(src)
            ?: throw IllegalArgumentException("Unable to serialize ColorStateList without JsonSerializationContext")
    }

    // The deserialize method converts a JsonElement object into a ColorStateList object, which
    // can be used to deserialize the object from a JSON string. This method takes a JsonElement
    // object as input, along with its corresponding type and a JsonDeserializationContext object.
    // If the context object is null, the method throws an IllegalArgumentException.
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): ColorStateList {
        // The deserialize method uses the context object to deserialize the JsonElement object
        // and return it as a ColorStateList object.
        return context?.deserialize(json, typeOfT)
            ?: throw IllegalArgumentException("Unable to deserialize ColorStateList without JsonDeserializationContext")
    }
}
