package com.codesteem.mylauncher.gesture.util

import java.util.ArrayDeque

/**
 * A thread-safe fixed-size queue implemented using an array. When the queue reaches its maximum size, the
 * first element (i.e. the element at index 0) will be removed to make room for the new element.
 *
 * @param <E> the type of elements held in this queue
 * @author more efficient implementation
 */
class FixedSizeQueue<E>(private val maxSize: Int) : ArrayDeque<E>() {

    /**
     * Adds the specified element to the end of this queue if it is possible to do so immediately without
     * violating capacity restrictions. When this queue reaches its maximum size, the first element
     * (i.e. the element at index 0) will be removed to make room for the new element.
     *
     * @param e the element to add
     * @return true if the element was added to this queue, false otherwise
     */
    override fun offer(e: E): Boolean {
        if (size >= maxSize) {
            poll() // poll() returns the head of the queue and removes it, which is more efficient than removeFirst()
        }

        return super.offer(e)
    }

    /**
     * Returns the element at the specified position in this queue.
     *
     * @param index the index of the element to return
     * @return the element at the specified position in this queue
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >= size())
     */
    override fun get(index: Int): E {
        checkIndex(index) // checkIndex() throws IndexOutOfBoundsException if the index is out of range
        return super.get(index)
    }

    /**
     * Returns the element at the specified position in this queue and removes it.
     *
     * @param index the index of the element to remove and return
     * @return the element at the specified position in this queue
