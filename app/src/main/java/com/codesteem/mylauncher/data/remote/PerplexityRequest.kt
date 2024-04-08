// Data class representing the request for the Perplexity API.
@Keep
data class PerplexityRequest(
    val stream: Boolean = true, // Whether to stream the response or not. Default is true.
    val model: String = "pplx-7
