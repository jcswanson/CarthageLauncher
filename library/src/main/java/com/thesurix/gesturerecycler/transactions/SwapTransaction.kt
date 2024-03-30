package com.thesurix.gesturerecycler.transactions

import com.thesurix.gesturerecycler.util.swap

/**
 * The SwapTransaction class is responsible for swapping two elements in a list-based data structure.
 * It implements the Transaction interface and provides perform and revert methods to switch
 * the elements' positions and switch them back, respectively.
 *
 * @param firstIndex The index of the first element to be swapped.
 * @param secondIndex The index of the second element to be swapped.
 * @param headerEnabled A flag indicating whether the data structure contains a header. If true,
 *                      the offset of the notifyChanged method calls will be increased by 1.
 *
 * @author thesurix
 */
class SwapTransaction<T>(private val firstIndex: Int,
                         private val secondIndex: Int,
                         private val headerEnabled: Boolean) : Transaction<T> {

    /**
     * Swaps the elements at the specified indices in the given data structure and updates the
     * UI accordingly.
     *
     * @param transactional An object implementing the Transactional interface, providing
     *                      access to the data structure and UI update methods.
     *
     * @return True, indicating the operation was successful.
     */
    override fun perform(transactional: Transactional<T>): Boolean {
        transactional.data.swap(firstIndex, secondIndex) // Swap elements
        notifyChanged(transactional) // Notify UI about changes
        return true
    }

    /**
     * Swaps the elements at the specified indices in the given data structure back to their
     * original positions and updates the UI accordingly.
     *
     * @param transactional An object implementing the Transactional interface, providing
     *                      access to the data structure and UI update methods.
     *
     * @return True, indicating the operation was successful.
     */
    override fun revert(transactional: Transactional<T>): Boolean {
        transactional.data.swap(secondIndex, firstIndex) // Swap elements back
        notifyChanged(transactional) // Not
