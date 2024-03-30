package com.codesteem.mylauncher.models

import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable

/**
 * Data class representing information about an app, including its name and icon.
 * The icon is stored as a byte array, allowing for flexibility in its usage.
 * Implements the Parcelable interface for efficient data transfer between app components.
 */
data class AppInfo(
    val appName: String, // Name of the app
    val appIconByteArray: ByteArray // Icon of the app, stored as a byte array
) : Parcelable {

    /**

