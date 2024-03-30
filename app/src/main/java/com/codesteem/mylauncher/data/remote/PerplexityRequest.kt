// Data class representing the request for a Perplexity API call
// Contains a boolean flag 'stream' to indicate if streaming response is required,
// a string 'model' to specify the model to be used for the API call,
// and a list of 'Message' objects 'messages' containing the prompts for the API call.
@Keep
data class PerplexityRequest(
    val stream: Boolean = true, // Set to true by default to enable streaming response
    val model: String = "pplx-7
