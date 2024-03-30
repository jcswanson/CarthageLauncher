package com.thesurix.gesturerecycler.transactions

/**
 * The InsertTransaction class is responsible for handling the insertion of a new item into a list
 * within a [Transactional] object. It keeps track of the item to be inserted, its position, and
 * whether the header is enabled.
 *
 * @param item The item to be inserted into the list.
 * @param position The position at which the item will be inserted.
 * @param headerEnabled A flag indicating whether the header is enabled or not.
 * @author thesurix
 */
class InsertTransaction<T>(private val item: T,
                           private val position: Int,
                           private val headerEnabled: Boolean) : Transaction<T> {

    /**
     * Performs the insertion of the item into the list at the specified position. If the header is
     * enabled, the item will be inserted after the header. After inserting the item, the method
     * notifies the [Transactional] object of the insertion by calling [Transactional.notifyInserted].
     *
     * @param transactional The [Transactional] object that contains the list where the item will be
     * inserted.
     * @return True if the insertion was successful, false otherwise.
     */
    override fun perform(transactional: Transactional<T>): Boolean {
        return with(transactional.data) {
            add(position, item) // Inserts the item at the specified position.
            val insertedPosition = position + if (headerEnabled) 1 else 0
            transactional.notifyInserted(insertedPosition) // Notifies the insertion.
            true
        }
    }

    /**
     * Reverts the insertion of the item by removing it from the list. If the header is enabled, the
     * item will be removed from after the header. After removing the item, the method notifies the
     * [Transactional] object of the removal by calling [Transactional.notifyRemoved].
     *
     * @param transactional The [Transactional] object that contains the list where the item was
     * inserted.
     * @return True if the removal was successful, false otherwise.
