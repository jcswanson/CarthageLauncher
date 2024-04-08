package com.codesteem.mylauncher.gesture.util

import android.view.View
import androidx.core.view.isVisible

/**
 * This function swaps two items inside a MutableList.
 * It takes two integer parameters, `firstIndex` and `secondIndex`,
 * which represent the indices of the items to swap.
 *
 * @param firstIndex The index of the first item to swap.
 * @param secondIndex The index of the second item to swap.
 */
fun <T> MutableList<T>.swap(firstIndex: Int, secondIndex: Int) {
    // Temporarily store the value of the first index
    val tmp = this[firstIndex]
    
    // Set the value of the first index to the value of the second index
    this[firstIndex] = this[secondIndex]
    
    // Set the value of the second index to the temporarily stored value
    this[secondIndex] = tmp
}

/**
 * This function calculates the data offset based on the state of the header.
 * It takes a boolean parameter, `headerEnabled`, which indicates whether the header is enabled or not.
 *
 * @param headerEnabled A flag indicating whether the header is enabled (true) or not (false).
 * @return The calculated data offset as an integer value.
 */
fun getDataOffset(headerEnabled: Boolean): Int {
    return if (headerEnabled) -1 else 0
}

/**
 * This extension function sets the visibility of a View to 'VISIBLE' if it's not already visible.
 * It takes no parameters and returns nothing.
 *
 * @receiver The View whose visibility will be changed.
 */
fun View.visible() {
    if (!this.isVisible) {
        // Set the visibility of the View to 'VISIBLE'.
        this.visibility = View.VISIBLE
    }
}
