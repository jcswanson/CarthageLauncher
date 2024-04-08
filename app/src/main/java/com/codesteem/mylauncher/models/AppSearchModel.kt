package com.codesteem.mylauncher.models

import android.graphics.Bitmap
import android.graphics.drawable.Drawable

// AppSearchModel is a data class that represents a model for app search results
data class AppSearchModel(

    // The title of the app
    val title: String?="",

    // The user image associated with the app
    val userImage: Drawable,

    // The package name of the app
    val packageName: String?="",

)

// Data classes in Kotlin automatically generate useful functions such as 'equals', 'hashCode' and 'copy'
// These functions help with comparing instances, creating new instances with modified values and more
