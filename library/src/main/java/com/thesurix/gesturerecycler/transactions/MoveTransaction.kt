package com.thesurix.gesturerecycler.transactions

/**
 * A class that represents a move transaction for a generic type T.
 * This class is used to move an item from one position to another within a list.
 *
 * @author thesurix
 */
class MoveTransaction<T>(
    /**
     * The index of the item to be moved.
     */
    private val from: Int,

    /**
     * The index to which the item will be moved.
     */
    private val to: Int,

    /**
     * A flag indicating whether the list has a header or not.
     * This is used to adjust the index of the moved item.
     */
    private val headerEnabled: Boolean
) : Transaction<T> {

    /**
     * Performs the move transaction on the given [transactional] data.
     * This method removes the item at the [from] index, adds it to the [to] index,
     * and notifies the adapter of the move.
     *
     * @param transactional The data on which the transaction will be performed.
     * @return True if the transaction was successful, false otherwise.
     */
    override fun perform(transactional: Transactional<T>): Boolean {
        return with(transactional.data) {
            // Remove the item at the from index
            val removedItem = removeAt(from)
            // Add the removed item to the to index
            removedItem?.let {
                add(to, it)
                // Calculate the moved offset based on the headerEnabled flag
                val movedOffset = if (headerEnabled) 1 else 0
                // Notify the adapter of the move
                transactional.notifyMoved(from + movedOffset, to + movedOffset)
                // Return true if the transaction was successful
                true
            } ?: false
        }
    }

    /**
     * Reverts the move transaction on the given [transactional] data.
     * This method removes the item at the [to] index, adds it to the [from] index,
     * and notifies the adapter
