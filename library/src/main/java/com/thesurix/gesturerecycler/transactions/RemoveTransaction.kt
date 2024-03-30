package com.thesurix.gesturerecycler.transactions

/**
 * The RemoveTransaction class is responsible for removing an item from a list and handling the
 * necessary notifications for the RecyclerView. This class is parameterized with the type of
 * items in the list.
 *
 * @param position The position of the item to be removed.
 * @param headerEnabled A flag indicating whether the list contains headers or not.
 * @author thesurix
 */
class RemoveTransaction<T>(private val position: Int,
                           private val headerEnabled: Boolean) : Transaction<T> {

    // The item to be removed
    private var item: T? = null

    /**
     * Performs the removal of the item at the specified position and notifies the RecyclerView
     * about the change.
     *
     * @param transactional The Transactional instance containing the data list and notification
     * methods.
     * @return True if an item was removed, false otherwise.
     */
    override fun perform(transactional: Transactional<T>): Boolean {
        return with(transactional.data) {
            // Remove the item at the specified position
            val removedItem = removeAt(position)
            // If an item was removed, update the item variable and notify the RecyclerView
            removedItem?.let {
                item = it
                transactional.notifyRemoved(position + if(headerEnabled) 1 else 0)
                true
            } ?: false
        }
    }

    /**
     * Reverts the removal of the item and notifies the RecyclerView about the change.
     *
     * @param transactional The Transactional instance containing the data list and notification
     * methods.
     * @return True if an item was added, false otherwise.
     */
    override fun revert(transactional: Transactional<T>): Boolean {
        return with(transactional.data) {
            // If an item was removed, add it back to the list and notify the RecyclerView
            item?.let {
                add(position, it)
                transactional.notifyInserted(position
