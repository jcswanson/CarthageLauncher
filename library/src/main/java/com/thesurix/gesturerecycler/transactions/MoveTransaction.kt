package com.thesurix.gesturerecycler.transactions

/**
 * A class that represents a move transaction for a generic type T.
 * This class is used to move an item from one position to another within a list.
 *
 * @author thesurix
 */
class MoveTransaction<T>(
    private val from: Int,
    private val to: Int,
    private val headerEnabled: Boolean,
    private val transactional: Transactional<T>
) : Transaction<T> {

    /**
     * Performs the move transaction on the [transactional] data.
     * This method removes the item at the [from] index, adds it to the [to] index,
     * and notifies the adapter of the move.
     *
     * @return True if the transaction was successful, false otherwise.
     */
    override fun perform(): Boolean {
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
     * Reverts the move transaction on the [transactional] data.
     * This method removes the item at the [to] index, adds it to the [from] index,
     * and notifies the adapter.
     */
    override fun revert() {
        with(transactional.data) {
            // Remove the item at the to index
            val removedItem = removeAt(to)
            // Add the removed item to the from index
            removedItem?.let {
                add(from, it)
                // Calculate the moved offset based on the headerEnabled flag
                val movedOffset = if (headerEnabled) 1 else 0
                // Notify the adapter of the move
                transactional.notifyMoved(to + movedOffset, from + movedOffset)
            }
        }
    }
}
