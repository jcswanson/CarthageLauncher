package com.codesteem.mylauncher.models

import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable

/**
 * Data class representing information about an app, including its name and icon.
 * The icon is stored as a Drawable, allowing for easier manipulation and usage.
 * Implements the Parcelable interface for efficient data transfer between app components.
 */
data class AppInfo(
    val appName: String, // Name of the app
    val appIcon: Drawable // Icon of the app, stored as a Drawable
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readParcelable(Drawable::class.java.classLoader)!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(appName)
        parcel.writeParcelable(appIcon, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AppInfo> {
        override fun createFromParcel(parcel: Parcel): AppInfo {
            return AppInfo(parcel)
        }

        override fun newArray(size: Int): Array<AppInfo?> {
            return arrayOfNulls(size)
        }
    }
}
