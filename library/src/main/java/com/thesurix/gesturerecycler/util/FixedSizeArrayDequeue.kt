package com.thesurix.gesturerecycler.util

import java.util.*

/**
 * A custom ArrayDeque with a fixed maximum size. When the maximum size is reached, the head
 * element will be removed to make room for the new element. This class is useful when you want
 * to maintain a limited size queue while still having the flexibility and efficiency of an
 * ArrayDeque.
 *
 * @author thesurix
 */
class FixedSizeArrayDequeue<E>(private val maxSize: Int) : ArrayDeque<E>(maxSize) {

    /**
     * Overrides the default offer method to maintain the fixed size of the deque. If the size
     * of the deque is already at its maximum, the head element will be removed before adding
     * the new element.
     *
     * @param e The element to be added to the deque.
     * @return True if the element was added, false if the deque is full and the element was not added.
     */
    override fun offer(e: E): Boolean {
        if (size == maxSize) {
            // Remove the head element if the deque is full
            removeFirst()
        }

        // Add the new element
        return super.offer(e)
    }
}
