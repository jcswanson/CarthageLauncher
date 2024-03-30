// Data class representing the request for a Perplexity API call
// Contains a boolean flag 'stream' to indicate if streaming response is required,
// a non-empty string 'model' to specify the model to be used for the API call,
// and a non-empty list of 'Message' objects 'messages' containing the prompts for the API call.
@Keep
data class PerplexityRequest(
    val stream: Boolean = true, // Set to true by default to enable streaming response
    val model: String,
    @Json(ignoreIfNull = false)
    val messages: List<Message>
) {
    init {
        require(model.isNotEmpty()) { "Model must be a non-empty string." }
        require(messages.isNotEmpty()) { "Messages must be a non-empty list." }
    }
}
