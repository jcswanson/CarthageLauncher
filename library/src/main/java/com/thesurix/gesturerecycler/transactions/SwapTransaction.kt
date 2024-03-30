package com.thesurix.gesturerecycler.transactions

import com.thesurix.gesturerecycler.util.swap
import com.thesurix.gesturerecycler.transactions.Transactional.() -> Unit

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
class SwapTransaction<T>(
    private val firstIndex: Int,
    private val secondIndex: Int,
    private val headerEnabled: Boolean
) : Transaction<T> {

    override fun perform(transactional: Transactional<T>): Boolean {
        require(firstIndex in 0 until transactional.data.size) { "Invalid first index: $firstIndex" }
        require(secondIndex in 0 until transactional.data.size) { "Invalid second index: $secondIndex" }

        transactional.data.swap(firstIndex, secondIndex) // Swap elements
        notifyChanged(transactional) // Notify UI about changes
        return true
    }

    override fun revert(transactional: Transactional<T>): Boolean {
        require(firstIndex in 0 until transactional.data.size) { "Invalid first index: $firstIndex" }
        require(secondIndex in 0 until transactional.data.size) { "Invalid second index: $secondIndex" }

        transactional.data.swap(secondIndex, firstIndex) // Swap elements back
        notifyChanged(transactional) // Notify UI about changes
        return true
    }

    private fun notifyChanged(transactional: Transactional<T>) {
        if (headerEnabled) {
            transactional(
                { notifyItemChanged(1, null) },
                { notifyItemRangeChanged(1, itemCount - 1) },
                { notifyItemChanged(itemCount, null) }
            )
        } else {
            transactional(
                { notifyItemChanged(0, null) },
                { notifyItemRangeChanged(0, itemCount) },
                { notifyItemChanged(itemCount, null) }
            )
        }
    }
}

interface Transactional<T> {
    val data: MutableList<T>

    fun notifyItemChanged(position: Int, payload: Any? = null)
    fun notifyItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any? = null)
    fun notifyItemChanged(position: Int, payload: Any? = null) {
        // Placeholder implementation, should be overridden in the actual implementation
        throw UnsupportedOperationException("notifyItemChanged should be overridden")
    }

    fun notifyItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any? = null) {
        // Placeholder implementation, should be overridden in the actual implementation
        throw UnsupportedOperationException("notifyItemRangeChanged should be overridden")
    }
}
