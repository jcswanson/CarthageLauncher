package com.codesteem.mylauncher.test

// Importing necessary classes and interfaces
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes

// Defining Month class that implements MonthItem interface
class Month(
    // Overriding name property from MonthItem interface
    override val name: String,

    // drawableId property to store the Drawable object
    val drawableId: Drawable,

    // packageName property to store the package name
    val packageName: String,

    // counterNotification property to store the counter notification
    var counterNotification: Int? = 0, // Nullable integer to allow for optional notification count

    // openCounter property to store the open counter
    override val openCounter: Int // Inherited from MonthItem interface
) : MonthItem {

    // Overriding type property from MonthItem interface
    override val type: MonthItem.MonthItemType
        get() = MonthItem.MonthItemType.MONTH
}
