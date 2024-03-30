// Data class representing a single choice in a list of possible responses.
data class Choice(
    // The amount of time the user has to respond.
    val responseDeadline: Deadline,

    // The reason why this choice was the last one presented to the user, or null if it wasn't.
    val finishReason: String?,

    // The index of this choice in the list of possible responses.
    val choiceIndex: Int,

    // The message associated with this choice.
    val associatedMessage: Message
)

// Extension property to convert a Delta object to a Deadline object
val Delta.toDeadline: Deadline
    get() = Deadline(this.value)

// Data class representing a time duration.
data class Delta(val value: Long)

// Data class representing a time deadline.
data class Deadline(val value: Long)

// Data class representing a message.
data class Message(val content: String)
