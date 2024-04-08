package com.thesurix.gesturerecycler.transactions

/**
 * A class representing a MoveTransaction, which moves an item from one position to another
 * in a list while supporting undo/redo operations.
 *
 * @param from The starting position of the item to be moved.
 * @param to The destination position of the item to be moved.
 * @param headerEnabled A flag indicating whether the list has a header or not.
 * @author thesurix
 */
class MoveTransaction<T>(private val from: Int,
                         private val to: Int,
                         private val headerEnabled: Boolean) : Transaction<T> {

    /**
     * Performs the move operation by removing the item at the specified 'from' position and
     * adding it back to the list at the 'to' position. Notifies the adapter of the moved item.
     *
     * @param transactional An object implementing the Transactional interface, which provides
     *                      access to the data list and adapter.
     * @return True if the move operation was successful, false otherwise.
     */
    override fun perform(transactional: Transactional<T>): Boolean {
        return with(transactional.data) {
            val removedItem = removeAt(from)
            removedItem?.let {
                add(to, it)
                val movedOffset = if (headerEnabled) 1 else 0
                transactional.notifyMoved(from + movedOffset, to + movedOffset)
                true
            } ?: false
        }
    }

    /**
     * Reverts the move operation by moving the item back to its original position.
     * Notifies the adapter of the moved item.
     *
     * @param transactional An object implementing the Transactional interface, which provides
     *                      access to the data list and adapter.
     * @return True if the revert operation was successful, false otherwise.
     */
    override fun revert(transactional: Transactional<T>): Boolean {
        return with(transactional.data) {
            val removedItem = removeAt(to)
            removedItem?.let
