package com.codesteem.mylauncher.bug_report

data class Bug(
    var id: String = "",
    var title: String = "",
    var description: String = "",
    var media: ArrayList<String> = arrayListOf("")
)
