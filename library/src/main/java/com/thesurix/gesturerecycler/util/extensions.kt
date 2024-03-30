package com.thesurix.gesturerecycler.util

/**
 * Swaps two items inside a MutableList.
 *
 * @param firstIndex the index of the first item to swap
 * @param secondIndex the index of the second item to swap
 */
@Suppress("UNCHECKED_CAST")
fun <T> MutableList<T>.safeSwap(firstIndex: Int, secondIndex: Int) {
    // Check if the indices are within the list bounds
    require(firstIndex in 0 until size && secondIndex in 0 until size) {
        "Indices $firstIndex and $secondIndex are out of bounds for list of size $size"
    }

    // Temporarily store the value of the first index
    val tmp = this[firstIndex] as T
    
    // Set the value of the first index to the value of the second index
    this[firstIndex] = this[secondIndex] as T
    
    // Set the value of the second index to the temporarily stored value
    this[secondIndex] = tmp
}

/**
 * Returns the data offset based on the state of the header.
 *
 * @param headerEnabled a flag indicating whether the header is enabled (true) or not (false)
 * @return the data offset
 */
fun getDataOffset(headerEnabled: Boolean) = if (headerEnabled) -1 else 0
