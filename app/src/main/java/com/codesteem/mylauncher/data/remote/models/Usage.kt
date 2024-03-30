// A data class representing the usage statistics of a model.
data class Usage(

    // The number of completion tokens used by the model.
    val completion_tokens: Int,

    // The number of prompt tokens used by the model.
    val prompt_tokens: Int,

    // The total number of tokens used by the model.
    val total_tokens: Int
)
