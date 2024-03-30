// A data class representing the usage statistics of a model.
data class ModelUsageStatistics(

    // The number of completion tokens used by the model.
    val numberOfCompletionTokens: Int,

    // The number of prompt tokens used by the model.
    val numberOfPromptTokens: Int,

    // The total number of tokens used by the model.
    val totalNumberOfTokens: Int
)
