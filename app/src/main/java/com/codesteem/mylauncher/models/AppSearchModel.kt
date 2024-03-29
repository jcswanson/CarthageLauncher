package com.codesteem.mylauncher.models

import android.graphics.Bitmap
import android.graphics.drawable.Drawable

data class AppSearchModel(
    val title: String?="",
    val userImage: Drawable,
    val packageName: String?="",

)
