package com.codesteem.mylauncher.models

import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable

/**
 * Data class representing information about an app, including its name and icon.
 * The icon is stored as a byte array, which can be used to reconstruct the Drawable.
 */
data class AppInfo(
    val appName: String, // The name of the app
    val appIconByteArray: ByteArray // The byte array representation of the app icon
) : Parcelable {

    /**
     * Constructs an instance of AppInfo from a Parcel.
     * This constructor is used during the parcelization and unparcelization process.
     */
    constructor(parcel: Parcel) : this(
        parcel.readString()!!, // Read the app name from the parcel
        parcel.createByteArray()!! // Read the app icon byte array from the parcel
    )

    /**
     * Writes the AppInfo instance to a Parcel for marshalling.
     * This method is used during the parcelization process.
     */
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(appName) // Write the app name to the parcel
        parcel.writeByteArray(appIconByteArray) // Write the app icon byte array to the parcel
    }

    /**
     * Describes the kinds of special objects contained in this Parcelable instance's marshaled representation.
     * This method is used during the parcelization process.
     * In this case, it always returns 0, as there are no special objects.
     */
    override fun describeContents(): Int {
        return 0
    }

    /**
     * The [CREATOR] object that creates new AppInfo objects from a Parcel.
     * This object is used during the unparcelization process.
     */
    companion object CREATOR : Parcelable.Creator<AppInfo> {
        /**
         * Creates a new AppInfo instance from a Parcel
