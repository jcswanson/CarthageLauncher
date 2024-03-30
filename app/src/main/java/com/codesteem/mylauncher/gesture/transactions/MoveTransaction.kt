package com.codesteem.mylauncher.gesture.transactions

/**
 * A class representing a MoveTransaction, which moves an item from one position to another
 * in a list-like data structure.
 *
 * @param from The starting position of the item to be moved.
 * @param to The destination position of the item to be moved.
 * @param headerEnabled A flag indicating whether the list has a header or not. If true, the
 * index of the items will be adjusted by 1 to account for the header.
 * @author thesurix
 */
class MoveTransaction<T>(private val from: Int,
                         private val to: Int,
                         private val headerEnabled: Boolean) : Transaction<T> {

    /**
     * Performs the move transaction by removing the item at the specified 'from' position and
     * adding it back to the list at the 'to' position. Notifies the observer of the moved item
     * using the provided Transactional interface.
     *
     * @param transactional The Transactional object containing the data and observer to be
     * notified of the transaction.
     * @return True if the item was successfully moved, false otherwise.
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
     * Reverts the move transaction by moving the item back to its original position. Notifies
     * the observer of the moved item using the provided Transactional interface.
     *
     * @param transactional The Transactional object containing the data and observer to be
     * notified of the transaction.
     * @return True if the item was successfully moved back, false otherwise.
     */
    override fun revert(transactional: Transactional<T>): Boolean {
        return
