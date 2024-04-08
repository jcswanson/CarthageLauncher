package com.thesurix.gesturerecycler.util

import java.util.*

/**
 * A fixed-size implementation of the Deque interface, backed by an array. This implementation differs from the standard ArrayDeque by limiting the size of the deque to a specified maximum value.
 *
 * @author thesurix
 */
class FixedSizeArrayDequeue<E>(private val maxSize: Int) : ArrayDeque<E>(maxSize) {

    /**
     * Inserts the specified element at the end of this deque if it is possible to do so immediately without violating capacity restrictions.
     * When the deque reaches its maximum size, the first element will be removed to make room for the new element.
     *
     * @param e the element to add
     * @return `true` if the element was added to this deque, else `false`
     */
    override fun offer(e: E): Boolean {
        if (size == maxSize) {
            removeFirst()
        }

        return super.offer(e)
    }
}
