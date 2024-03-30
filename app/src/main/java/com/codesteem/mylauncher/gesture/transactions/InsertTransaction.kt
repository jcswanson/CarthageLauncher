package com.codesteem.mylauncher.gesture.transactions

/**
 * The `InsertTransaction` class is responsible for handling the transaction of inserting an item into a list
 * at a specific position, while also considering whether a header is enabled or not.
 *
 * @author thesurix
 */
class InsertTransaction<T>(
    private val item: T, // The item to be inserted
    private val position: Int, // The position where the item will be inserted
    private val headerEnabled: Boolean // A flag to indicate if a header is enabled in the list
) : Transaction<T> {

    /**
     * Performs the transaction by adding the item to the list at the specified position,
     * notifying the inserted position, and returning true if the operation is successful.
     *
     * @param transactional The Transactional object containing the data to be modified.
     * @return true if the item is inserted successfully, false otherwise.
     */
    override fun perform(transactional: Transactional<T>): Boolean {
        return with(transactional.data) {
            add(position, item) // Adds the item to the list at the specified position
            val insertedPosition = if (headerEnabled) position + 1 else position
            transactional.notifyInserted(insertedPosition) // Notifies the inserted position
            true
        }
    }

    /**
     * Reverts the transaction by removing the item from the list at the specified position,
     * notifying the removed position, and returning true if the operation is successful.
     *
     * @param transactional The Transactional object containing the data to be reverted.
     * @return true if the item is removed successfully, false otherwise.
     */
    override fun revert(transactional: Transactional<T>): Boolean {
        return with(transactional.data) {
            if (position !in indices) {
                return@with true
            }
            val removedItem = removeAt(position) // Removes the item from the list at the specified position
            removedItem?.let {
                val removedPosition = if (headerEnabled) position + 1 else position
                transactional.notifyRemoved(removedPosition) // Notifies the removed position
            }
            true
        }
    }
}
