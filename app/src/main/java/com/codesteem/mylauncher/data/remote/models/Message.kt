// A data class representing a message object with two properties:
// - content: The actual message as a string.
// - role: The role of the message, such as 'assistant' or 'user'.
data class Message(

    // The content of the message. This is a required property and should not be null.
    val content: String,

    // The role of the message. This is a required property and should not be null.
    val role: String
)
