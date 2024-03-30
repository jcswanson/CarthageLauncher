package com.codesteem.mylauncher.gesture.transactions

/**
 * The AddTransaction class is responsible for adding an item to a Transactional data set and
 * notifying any observers of the change. This class is used in the context of a transaction,
 * which is a way to manage changes to an object's state in a consistent and reliable manner.
 *
 * @param item The item to be added to the Transactional data set.
 * @param headerEnabled A boolean value that indicates whether the data set has a header row.
 *                      If true, the size reported to observers will include the header row.
 */
class AddTransaction<T>(private val item: T,
                        private val headerEnabled: Boolean) : Transaction<T> {

    /**
     * This method adds the item to the Transactional data set and returns a boolean value
     * indicating whether the operation was successful. If the operation is successful, the
     * method notifies any observers of the change by calling the notifyInserted method.
     *
     * @param transactional The Transactional object that contains the data set to which the
     *                      item will be added.
     * @return A boolean value indicating whether the operation was successful.
     */
    override fun perform(transactional: Transactional<T>): Boolean {
        return with(transactional.data) {
            val success = add(item)
            if (success) {
                transactional.notifyInserted(if (headerEnabled) size else size - 1)
            }
            success
        }
    }

    /**
     * This method removes the last item added to the Transactional data set and returns a
     * boolean value indicating whether the operation was successful. If the operation is
     * successful, the method notifies any observers of the change by calling the notifyRemoved
     * method.
     *
     * @param transactional The Transactional object that contains the data set from which the
     *                      item will be removed.
     * @return A boolean value indicating whether the operation was successful.
    
