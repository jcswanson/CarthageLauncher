package com.codesteem.mylauncher.data.remote.models

data class Choice(
    val delta: Delta,
    val finish_reason: String?,
    val index: Int,
    val message: Message
)