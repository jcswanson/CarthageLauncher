package com.codesteem.mylauncher.bug_report

// `Bug` data class representing a single bug report
data class Bug(
    // Unique identifier for the bug report
    var id: String = "",

    // Title or short summary of the bug
    var title: String = "",

    // Detailed description of the bug, including steps to reproduce and expected vs. actual behavior
    var description: String = "",

    // List of media file paths (screenshots, logs, etc.) related to the bug report
    var media: ArrayList<String> = arrayListOf("")
)

