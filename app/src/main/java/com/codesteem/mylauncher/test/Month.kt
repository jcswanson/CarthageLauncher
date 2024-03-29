package com.codesteem.mylauncher.test


import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes

class Month(override val name: String, val drawableId: Drawable, val packageName: String, var counterNotification: Int?=0,
            override val openCounter: Int
) : MonthItem {

    override val type: MonthItem.MonthItemType
        get() = MonthItem.MonthItemType.MONTH
}
