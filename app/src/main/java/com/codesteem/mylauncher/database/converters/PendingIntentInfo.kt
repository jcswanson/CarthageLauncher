package com.codesteem.mylauncher.database.converters

import android.content.Intent

data class PendingIntentInfo(
    val requestCode: Int,
    val intent: Intent,
    val flags: Int
    // Add other necessary information
)