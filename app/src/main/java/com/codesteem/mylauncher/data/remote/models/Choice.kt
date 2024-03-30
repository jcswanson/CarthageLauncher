// Data class representing a single choice in a list of possible responses.
data class Choice(

    // The amount of time the user has to respond, represented as a Delta object.
    val delta: Delta,

    // The reason why this choice was the last one presented to the user, or null if it wasn't.
    val finish_reason: String?,

    // The index of this choice in the list of possible responses.
    val index: Int,

    // The message associated with this choice, represented as a Message object.
    val message: Message
)
