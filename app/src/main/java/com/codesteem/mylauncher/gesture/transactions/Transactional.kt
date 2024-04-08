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

    /**
     * Notifies listeners that the data item at the specified position has changed.
     * This method should be called when the data item at the given position has been modified.
     *
     * @param position The position of the data item that has changed.
     */
    fun notifyChanged(position: Int)

    /**
     * Notifies listeners that a new data item has been inserted at the specified position.
     * This method should be called when a new data item has been added to the list.
     *
     * @param position The position at which the new data item has been inserted.
     */
    fun notifyInserted(position: Int)

    /**
     * Notifies listeners that the data item at the specified position has been removed.
     * This method should be called when a data item has been removed from the list.
     *
     * @param position The position of the data item that has been removed.
     */
    fun notifyRemoved(position: Int)

    /**
     * Notifies listeners that the data item at the specified position has been moved to a new position.
     * This method should be called when a data item has been moved within the list.
     *
     * @param fromPosition The original position of the data item that has been moved.
     * @param toPosition The new position of the data item that has been moved.
     */
    fun notifyMoved(fromPosition: Int, toPosition: Int)
}
