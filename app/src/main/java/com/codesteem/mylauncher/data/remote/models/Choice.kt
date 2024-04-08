// Data class representing a single choice in a list of possible responses.
data class Choice(

    // The amount of time the user has to respond, represented as a Delta object.
    val delta: Delta,

    // The reason why the assistant finished generating this choice, if applicable.
    val finish_reason: String?,

    // The index of this choice in the list of possible responses.
    val index: Int,

    // The message associated with this choice, which may include text or other media.
    val message: Message
)
