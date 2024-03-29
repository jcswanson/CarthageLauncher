package com.codesteem.mylauncher.data.remote

import androidx.annotation.Keep

@Keep
data class PerplexityRequest(
    val stream: Boolean = true,
    val model: String = "pplx-70b-online",
    val messages: List<Message> = listOf(
        Message()
    )
)

@Keep
data class Message(
    val role: String = "system",
    val content: String = "Be precise and concise.",
)

