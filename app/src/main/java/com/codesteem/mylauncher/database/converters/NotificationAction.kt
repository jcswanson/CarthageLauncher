package com.codesteem.mylauncher.database.converters

import android.app.PendingIntent

data class NotificationActionDetails(
    val title: CharSequence?,
    val actionIntent: PendingIntent?
)