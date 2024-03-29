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

class MainRepository @Inject constructor(
) {

    private val _perplexityFlow = MutableStateFlow<String?>(null)
    val perplexityFlow = _perplexityFlow.asStateFlow()


    fun askPerplexity(perplexityRequest: PerplexityRequest){
        val eventSourceListener = object : EventSourceListener() {
            override fun onOpen(eventSource: EventSource, response: Response) {
                println("Connection opened")
            }

            override fun onClosed(eventSource: EventSource) {
                println("Connection closed")
            }

            override fun onEvent(
                eventSource: EventSource,
                id: String?,
                type: String?,
                data: String
            ) {
                super.onEvent(eventSource, id, type, data)
                _perplexityFlow.value = data
                println("Received message: $data")
            }

            override fun onFailure(
                eventSource: EventSource,
                t: Throwable?,
                response: Response?
            ) {
                super.onFailure(eventSource, t, response)
                println("Failure: $response, throwable: $t")
            }
        }

        val requestBody = Gson().toJson(perplexityRequest).toRequestBody("application/json".toMediaTypeOrNull())

        val client = OkHttpClient.Builder().connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.MINUTES)
            .writeTimeout(10, TimeUnit.MINUTES)
            .build()

        val sseRequest = Request.Builder()
            .url("https://api.perplexity.ai/chat/completions")
            .header("Accept", "text/event-stream")
            .header("Authorization", "Bearer ${BuildConfig.PERPLEXITY_API_KEY}")
            .post(requestBody)
            .build()

        val eventSource = EventSources.createFactory(client)
            .newEventSource(sseRequest, eventSourceListener)


        eventSource.request()
    }
}