package com.thesurix.gesturerecycler.transactions

/**
 * The RemoveTransaction class is responsible for performing and reverting the removal of an item
 * from a list in a transactional manner. This class is generic, allowing it to work with any
 * type of data (represented by the type parameter T).
 *
 * @author thesurix
 */
class RemoveTransaction<T>(
    private val position: Int, // The position of the item to be removed
    private val headerEnabled: Boolean // A flag indicating whether the list has a header
) : Transaction<T> {

    // The removed item, kept for revert operation
    private var item: T? = null

    /**
     * Performs the removal of the item at the specified position from the given data list.
     * If the item was successfully removed, the corresponding item removal is notified to
     * the Transactional object.
     *
     * @param transactional The Transactional object containing the data list to be modified
     * @return true if the item was removed, false otherwise
     */
    override fun perform(transactional: Transactional<T>): Boolean {
        return with(transactional.data) {
            val removedItem = removeAt(position) // Remove the item at the specified position
            removedItem?.let {
                item = it // Save the removed item for revert operation
                transactional.notifyRemoved(position + if (headerEnabled) 1 else 0) // Notify the removal
                true
            } ?: false
        }
    }

    /**
     * Reverts the removal of the item by adding it back to the data list at the same position.
     * If the item was successfully added back, the corresponding item insertion is notified
     * to the Transactional object.
     *
     * @param transactional The Transactional object containing the data list to be modified
     * @return true if the item was added back, false otherwise
     */
    override fun revert(transactional: Transactional<T>): Boolean {
        return with(transactional.data) {
            item?.let {
               
