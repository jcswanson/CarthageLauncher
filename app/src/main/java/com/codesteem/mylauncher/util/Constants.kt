package com.codesteem.mylauncher.util

/**
 * This object contains various constants used throughout the `com.codesteem.mylauncher` package.
 * By defining these values as constants, we can ensure that they are consistent and avoid
 * hard-coding these values throughout the codebase.
 */
object Constants {

    /**
     * The base URL for the Perplexity API. This URL is used as the starting point for all API
     * requests made to the Perplexity API.
     */
    @Suppress("SpellCheckingInspection")
    const val BASE_URL: String = "https://api.perplexity.ai/"

    /**
     * A string constant representing the category for bugs. This constant may be used to
     * categorize or filter items related to bugs or issues in the application.
     */
    const val BUGS: String = "Bugs"

    /**
     * A string constant representing the version of the Perplexity API. This constant may be used to
     * track and manage changes to the API.
     */
    const val VERSION: String = "v1"

    /**
     * A string constant representing the user agent of the client application. This constant may be used to
     * identify and log the client application in the server.
     */
    const val USER_AGENT: String = "MyLauncher/${BuildConfig.VERSION_NAME
