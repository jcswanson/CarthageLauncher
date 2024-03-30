package com.codesteem.mylauncher.models

import android.graphics.Bitmap
import android.graphics.drawable.Drawable

data class AppSearchModel(
    // The name of the app or activity
    val title: String? = "",

    // The user-facing image for the app or activity
    val userImage: Drawable,

    // The package name of the app or activity
    val packageName: String? = ""
)

// This data class represents a model for an app or activity search result
// It contains three properties:
// - title: The name of the app or activity
// - userImage: The user-facing image for the app or activity
// - packageName: The package name of the app or activity
