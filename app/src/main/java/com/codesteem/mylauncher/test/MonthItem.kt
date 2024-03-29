package com.codesteem.mylauncher.test

interface MonthItem {

    val type: MonthItemType

    val name: String

    val openCounter: Int

    enum class MonthItemType {
        HEADER, MONTH
    }
}
