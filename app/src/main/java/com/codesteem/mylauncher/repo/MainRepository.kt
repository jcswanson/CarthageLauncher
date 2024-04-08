package com.codesteem.mylauncher.repo

import com.codesteem.mylauncher.BuildConfig
import com.codesteem.mylauncher.data.remote.PerplexityRequest
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources
import java.util.concurrent.TimeUnit
import javax.inject.Inject

// MainRepository class is responsible for handling the Perplexity API request and updating the flow with the response data
class MainRepository @Inject constructor(
) {

    // MutableStateFlow to hold the perplexity response data
    private val _perplexityFlow = MutableStateFlow<String?>(null)
    // Expose the flow as a read-only state flow
    val perplexityFlow = _perplexityFlow.asStateFlow()

    // Function to ask Perplexity API with a given PerplexityRequest
    fun askPerplexity(perplexityRequest: PerplexityRequest){
        // Create an instance of EventSourceListener to listen for events from the EventSource
        val eventSourceListener = object : EventSourceListener() {
            // Called when the connection is opened
            override fun onOpen(eventSource: EventSource, response: Response) {
                println("Connection opened")
            }

            // Called when the connection is closed
            override fun onClosed(eventSource: EventSource) {
                println("Connection closed")
            }

            // Called when a new event is received
            override fun onEvent(
                eventSource: EventSource,
                id: String?,
                type: String?,
                data: String
            ) {
                super.onEvent(eventSource, id, type, data)
                // Update the flow with the new data
                _perplexityFlow.value = data
                println("Received message: $data")
            }

            // Called when an error occurs
            override fun onFailure(
                eventSource: EventSource,
                t: Throwable?,
                response: Response?
            ) {
                super.onFailure(eventSource, t, response)
                println("Failure: $response, throwable: $t")
            }
        }

        // Convert the PerplexityRequest to JSON and create a request body
        val requestBody = Gson().toJson(perplexityRequest).toRequestBody("application/json".toMediaTypeOrNull())

        // Create an OkHttpClient instance with custom timeouts
        val client = OkHttpClient.Builder().connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.MINUTES)
            .writeTimeout(10, TimeUnit.MINUTES)
            .build()

        // Create the API request
        val sseRequest = Request.Builder()
            .url("https://api.perplexity.ai/chat/completions")
            .header("Accept", "text/event-stream")
            .header("Authorization", "Bearer ${BuildConfig.PERPLEXITY_API_KEY}")
            .post(requestBody)
            .build()

        // Create the EventSource instance and start listening for events
        val eventSource = EventSources.createFactory(client)
            .newEventSource(sseRequest, eventSourceListener)

        eventSource.request()
    }
}
