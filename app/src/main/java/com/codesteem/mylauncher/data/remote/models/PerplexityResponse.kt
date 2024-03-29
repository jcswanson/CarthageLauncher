package com.codesteem.mylauncher.data.remote.models

data class PerplexityResponse(
    val choices: List<Choice>,
    val created: Int,
    val id: String,
    val model: String,
    val `object`: String,
    val usage: Usage
)