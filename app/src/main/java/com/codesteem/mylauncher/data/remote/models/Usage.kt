// Data class representing usage information for a model.
data class Usage(

    // The number of completion tokens used.
    val completion_tokens: Int,

    // The number of prompt tokens used.
    val prompt_tokens: Int,

    // The total number of tokens used.
    val total_tokens: Int
)
