package com.codesteem.mylauncher.bug_report

// Data class representing a bug report
data class Bug(
    // Unique identifier for the bug report
    var id: String = "",

    // Title or short summary of the bug
    var title: String = "",

    // Detailed description of the bug, including steps to reproduce and expected vs. actual behavior
    var description: String = "",

    // Array list of media file paths associated with the bug report, such as screenshots or log files
    var media: ArrayList<String> = arrayListOf("")
)

