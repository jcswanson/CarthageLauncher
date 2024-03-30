package com.codesteem.mylauncher.gesture.transactions

/**
 * Interface for objects that can perform transactional operations on a list of data items.
 *
 * @author thesurix
 */
interface Transactional<T> {
    /**
     * The list of data items that this transactional object manages.
     * This list is mutable, meaning that it can be modified directly.
     */
    val data: MutableList<T>
        private set

    /**
     * Notifies listeners that the data at the specified position has changed.
     * This method should be called when the data at the given position has been modified.
     *
     * @param position The position of the data that has changed.
     */
    fun notifyChanged(position: Int) {
        onDataChanged(position, 1)
    }

    /**
     * Notifies listeners that the data in the specified range has changed.
     * This method should be called when multiple data items have been modified.
     *
     * @param startPosition The starting position of the range that has changed.
     * @param itemCount The number of items in the changed range.
     */
    fun notifyChanged(startPosition: Int, itemCount: Int) {
        onDataChanged(startPosition, itemCount)
    }

    /**
     * Notifies listeners that a new data item has been inserted at the specified position.
     * This method should be called when a new data item has been added to the list.
     *
     * @param position The position where the new data item has been inserted.
     */
    fun notifyInserted(position: Int) {
        onDataChanged(position, 0, 1)
    }

    /**

