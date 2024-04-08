package com.codesteem.mylauncher.gesture.util

import java.util.*

/**
 * A fixed-size implementation of the Deque interface, backed by an array. This class ensures that the deque
 * never exceeds a specified maximum size by removing the first element when a new element is added and
 * the deque is already at maximum size.
 *
 * @author thesurix
 */
class FixedSizeArrayDequeue<E>(private val maxSize: Int) : ArrayDeque<E>(maxSize) {

    /**
     * Adds the specified element to the end of this deque if it is possible to do so immediately without
     * violating capacity restrictions. When this deque reaches its maximum size, the first element
     * (i.e. the element at index 0) will be removed to make room for the new element.
     *
     * @param e the element to add
     * @return true if the element was added to this deque, false otherwise
     */
    override fun offer(e: E): Boolean {
        if (size == maxSize) {
            removeFirst()
        }

        return super.offer(e)
    }
}
