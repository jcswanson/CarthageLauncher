package com.thesurix.gesturerecycler.util

import java.util.concurrent.ConcurrentLinkedDeque

/**
 * A thread-safe custom ArrayDeque with a fixed maximum size. When the maximum size is reached,
 * the head element will be removed to make room for the new element. This class is useful when
 * you want to maintain a limited size queue while still having the flexibility and efficiency of an
 * ArrayDeque.
 *
 * @author thesurix
 */
class FixedSizeArrayDequeue<E>(private val maxSize: Int) : ConcurrentLinkedDeque<E>() {

    /**
     * Overrides the default offer method to maintain the fixed size of the deque. If the size
     * of the deque is already at its maximum, the head element will be removed before adding
     * the new element.
     *
     * @param e The element to be added to the deque.
     * @return True if the element was added, false if the deque is full and the element was not added.
     */
    override fun offer(e: E): Boolean {
        while (size >= maxSize) {
            // Remove the head element if the deque is full
            poll()
        }

        // Add the new element
        return super.offer(e)
    }

    /**
     * A thread-safe version of the push method which adds an element to the front of the deque.
     *
     * @param e The element to be added to the front of the deque.
     */
    fun push(e: E) {
        addFirst(e)
    }

    /**
     * A thread-safe version of the pop method which removes and returns the head element of the deque.
     *
     * @return The head element of the deque.
     * @throws NoSuchElementException if the deque is empty.
     */
    fun pop(): E {
        return removeFirst()
    }
}
