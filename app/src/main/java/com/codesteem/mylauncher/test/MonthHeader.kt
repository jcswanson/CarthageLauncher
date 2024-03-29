package com.codesteem.mylauncher.test

class MonthHeader(override val name: String, override val openCounter: Int = 0) : MonthItem {

    override val type: MonthItem.MonthItemType
        get() = MonthItem.MonthItemType.HEADER
}
