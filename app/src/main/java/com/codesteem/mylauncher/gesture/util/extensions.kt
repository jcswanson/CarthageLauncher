package com.codesteem.mylauncher.gesture.util

import android.view.View
import androidx.core.view.isVisible

/**
 * This function swaps two items inside a MutableList.
 * It takes two integer parameters: the first index (firstIndex) and the second index (secondIndex)
 * to swap the elements at these positions within the list.
 *
 * @param firstIndex the index of the first item to swap
 * @param secondIndex the index of the second item to swap
 */
fun <T> MutableList<T>.swap(firstIndex: Int, secondIndex: Int) {
    // Temporarily store the value of the first index in a variable 'tmp'
    val tmp = this[firstIndex]
    // Set the value at the first index to the value at the second index
    this[firstIndex] = this[secondIndex]
    // Set the value at the second index to the value stored in 'tmp'
    this[secondIndex] = tmp
}

/**
 * This function calculates the data offset based on the header state.
 * It takes a boolean parameter 'headerEnabled' and returns an integer value.
 *
 * @param headerEnabled flag indicating whether the header is enabled or not
 * @return the data offset
 */
fun getDataOffset(headerEnabled: Boolean): Int {
    // If the header is enabled, return -1 as the data offset
    // Otherwise, return 0 as the data offset
    return if (headerEnabled) -1 else 0
}

/**
 * This extension function sets the visibility of a View to 'VISIBLE' if it's not already visible.
 * It takes a View parameter 'view' and checks if it's currently invisible.
 * If it is, the function sets its visibility to 'VISIBLE'.
 *
 * @param view the View to change the visibility for
 */
fun View.visible() {
    if (!this.isVisible) {
        this.visibility = View.VISIBLE
    }
}
