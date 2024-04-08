package com.codesteem.mylauncher

// Defining a sealed class named 'Resource' with 4 subclasses: Success, Error, Loading, and Initial
// The 'Resource' class is used to manage app resources, such as data and error messages
sealed class Resource<out T>(
    // The 'data' property holds the data returned from the API or any other source
    val data: T? = null,
    // The 'message' property holds an error message in case of an error
    val message: String? = null
) {
    // The 'Success' subclass represents a successful resource request
    // It takes the data as a parameter and sets it to the 'data' property
    class Success<T>(data: T?) : Resource<T>(data)

    // The 'Error' subclass represents a failed resource request
    // It takes an error message and optional data as parameters
    // The error message is set to the 'message' property, and the data (if provided) is set to the 'data' property
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)

    // The 'Loading' subclass represents a resource request that is currently in progress
    class Loading<T>() : Resource<T>()

    // The 'Initial' subclass represents the initial state of a resource before any request has been made
    class Initial<T>() : Resource<T>()
}
