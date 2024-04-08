package com.codesteem.mylauncher.gesture.transactions

/**
 * The `InsertTransaction` class is used to represent a transaction for inserting an item into a list
 * at a specific position, with an optional header. This class implements the `Transaction` interface
 * and provides implementations for the `perform` and `revert` methods.
 *
 * @param item The item to be inserted into the list.
 * @param position The position at which the item will be inserted.
 * @param headerEnabled A flag indicating whether the list has a header or not.
 * @author thesurix
 */
class InsertTransaction<T>(private val item: T,
                           private val position: Int,
                           private val headerEnabled: Boolean) : Transaction<T> {

    /**
     * This method performs the insertion of the item into the list at the specified position.
     * If the list has a header, the item will be inserted after the header.
     *
     * @param transactional An object implementing the `Transactional` interface, which provides
     *                      access to the list and its methods for adding and notifying changes.
     * @return `true` if the insertion was successful, `false` otherwise.
     */
    override fun perform(transactional: Transactional<T>): Boolean {
        return with(transactional.data) {
            add(position, item) // Adds the item to the list at the specified position.
            val insertedPosition = position + if (headerEnabled) 1 else 0
            transactional.notifyInserted(insertedPosition) // Notifies that an item has been inserted.
            true
        }
    }

    /**
     * This method reverts the insertion of the item from the list at the specified position.
     * If the list has a header, the item will be removed before the header.
     *
     * @param transactional An object implementing the `Transactional` interface, which provides
     *                      access to the list and its methods for removing and notifying changes.
     * @return `true` if the removal was successful, `false` otherwise.
     */
    override fun re
