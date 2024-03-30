// Share data class
//
// This class represents a share object that contains information about an app
// and associated data to be shared.
//
// Properties:
// - app: A string that specifies the name of the app to be shared.
// - data: A string that contains the data to be shared with the app.

package com.codesteem.mylauncher

data class Share(
    // The name of the app to be shared.
    val app: String,

    // The data to be shared with the app.
    val data: String
)
