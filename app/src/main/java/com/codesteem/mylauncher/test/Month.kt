package com.codesteem.mylauncher.test

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes

sealed class MonthItem(open val name: String, open val openCounter: Int) {
    enum class MonthItemType { MONTH, APP }
}

class Month(
    name: String,
    @DrawableRes drawableRes: Int,
    packageName: String,
    counterNotification: Int? = null,
    openCounter: Int
) : MonthItem(name, openCounter) {
    val drawableId: Drawable by lazy { appResources.getDrawable(drawableRes) }
    val packageName: String
    var counterNotification: Int?

    init {
        this.packageName = packageName
        this.counterNotification = counterNotification
    }
}

class App(
    name: String,
    @DrawableRes drawableRes: Int,
    packageName: String,
    counterNotification: Int? = null,
    openCounter: Int
) : MonthItem(name, openCounter) {
    val drawableId: Drawable by lazy { appResources.getDrawable(drawableRes) }
    val packageName: String
    var counterNotification: Int?

    init {
        this.packageName = packageName
        this.counterNotification = counterNotification
    }
}

val appResources by lazy {
    ApplicationProvider.getApplicationContext().resources
}
