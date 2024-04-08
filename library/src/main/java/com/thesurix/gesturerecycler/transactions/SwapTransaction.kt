package com.thesurix.gesturerecycler.transactions

import com.thesurix.gesturerecycler.util.swap

/**
 * A [Transaction] that swaps the elements of a [Transactional] list at two specified indices.
 *
 * @author thesurix
 */
class SwapTransaction<T>(
    private val firstIndex: Int, // index of the first element to swap
    private val secondIndex: Int, // index of the second element to swap
    private val headerEnabled: Boolean // whether the list has a header or not
) : Transaction<T> {

    /**
     * Performs the swap operation on the given [transactional] list.
     *
     * @return `true` if the swap was successful, `false` otherwise
     */
    override fun perform(transactional: Transactional<T>): Boolean {
        transactional.data.swap(firstIndex, secondIndex) // swap the elements
        notifyChanged(transactional) // notify the adapter of the changes
        return true
    }

    /**
     * Reverts the swap operation on the given [transactional] list.
     *
     * @return `true` if the revert was successful, `false` otherwise
     */
    override fun revert(transactional: Transactional<T>): Boolean {
        transactional.data.swap(secondIndex, firstIndex) // swap the elements back
        notifyChanged(transactional) // notify the adapter of the changes
        return true
    }

    /**
     * Notifies the adapter that the elements at the specified indices have changed.
     *
     * @param transactional the [Transactional] list that contains the changed elements
     */
    private fun notifyChanged(transactional: Transactional<T>) {
        val changedOffset = if (headerEnabled) 1 else 0 // adjust the offset if the list has a header
        transactional.notifyChanged(firstIndex + changedOffset) // notify the adapter of the first changed element
        transactional.notifyChanged(secondIndex + changedOffset) // notify the adapter of the second changed element
    }
}
