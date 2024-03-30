package com.thesurix.gesturerecycler.transactions

/**
 * A transaction for inserting an item into a list within a [Transactional] object.
 *
 * @param item The item to be inserted.
 * @param insertionIndex The index at which the item will be inserted.
 * @param headerEnabled A flag indicating whether the header is enabled.
 * @author thesurix
 */
class InsertTransaction<T>(
    private val item: T,
    private val insertionIndex: Int,
    private val headerEnabled: Boolean
) : Transaction<T> {

    /**
     * Inserts the item into the list at the specified index and notifies the [Transactional] object
     * of the insertion.
     *
     * @param transactional The [Transactional] object that contains the list where the item will be
     * inserted.
     * @return True if the insertion was successful, false otherwise.
     */
    override fun perform(transactional: Transactional<T>): Boolean {
        return transactional.data.apply {
            add(insertionIndex + if (headerEnabled) 1 else 0, item)
            transactional.notifyInserted(insertionIndex + if (headerEnabled) 1 else 0)
        }.size > insertionIndex
    }

    /**
     * Reverts the insertion of the item by removing it from the list and notifying the
     * [Transactional] object of the removal.
     *
     * @param transactional The [Transactional] object that contains the list where the item was
     * inserted.
     * @return True if the removal was successful, false otherwise.
     */
    override fun revert(transactional: Transactional<T>): Boolean {
        return transactional.data.apply {
            removeAt(insertionIndex + if (headerEnabled) 1 else 0)
            transactional.notifyRemoved(insertionIndex)
        }.size == size - 1
    }
}
