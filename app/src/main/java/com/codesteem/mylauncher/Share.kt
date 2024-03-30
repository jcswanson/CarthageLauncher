// Share data class
//
// This class represents a share object that contains information about an app
// and associated data to be shared.
//
// Properties:
// - appName: A non-empty string that specifies the name of the app to be shared.
// - shareData: A non-null string that contains the data to be shared with the app.

package com.codesteem.mylauncher

data class Share(
    // The name of the app to be shared.
    val appName: String,

    // The data to be shared with the app.
    val shareData: String
) {
    init {
        require(appName.isNotEmpty()) { "App name cannot be empty" }
        require(shareData != null) { "Share data cannot be null" }
    }
}
