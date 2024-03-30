package com.codesteem.mylauncher.models

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

// This data class represents a model for an app or activity search result
// It contains three properties:
// - title: The name of the app or activity
// - userImage: The user-facing image for the app or activity
// - packageName: The package name of the app or activity

@Parcelize
data class AppSearchModel(
    // The name of the app or activity
    val title: String? = "",

    // The user-facing image for the app or activity
    val userImage: Bitmap? = null, // Changed to Bitmap for better memory management

    // The package name of the app or activity
    val packageName: String? = ""
) : Parcelable

