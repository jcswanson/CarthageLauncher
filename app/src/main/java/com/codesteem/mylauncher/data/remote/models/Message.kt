// A data class representing a message object with two properties:
// - content: The actual message text as a String.
// - role: The role of the message, such as 'assistant' or 'user'.
enum class MessageRole { ASSISTANT, USER }

data class Message(
    val content: String, // The content of the message
    val role: MessageRole // The role of the message (e.g. 'assistant' or 'user')
) {
    init {
        require(role == MessageRole.ASSISTANT || role == MessageRole.USER) {
            "Invalid message role: $role. Allowed roles are 'ASSISTANT' and 'USER'."
        }
    }
}
