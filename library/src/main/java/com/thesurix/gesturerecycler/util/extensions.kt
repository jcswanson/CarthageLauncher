package com.thesurix.gesturerecycler.util

/**
 * This function swaps two items inside a MutableList.
 * It takes two integer arguments, `firstIndex` and `secondIndex`,
 * which represent the indices of the items to be swapped.
 *
 * @param firstIndex the index of the first item to swap
 * @param secondIndex the index of the second item to swap
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
 * This function returns the data offset based on the state of the header.
 * It takes a boolean argument, `headerEnabled`, which indicates whether the header is enabled or not.
 *
 * @param headerEnabled a flag indicating whether the header is enabled (true) or not (false)
 * @return the data offset
 */
fun getDataOffset(headerEnabled: Boolean) = if (headerEnabled) -1 else 0
