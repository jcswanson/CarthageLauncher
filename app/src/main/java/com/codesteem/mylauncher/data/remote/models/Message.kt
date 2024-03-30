// A data class representing a message object with two properties:
// - content: The actual message text as a String.
// - role: The role of the message, such as 'assistant' or 'user'.
data class Message(
    val content: String, // The content of the message
    val role: String // The role of the message (e.g. 'assistant' or 'user')
)
