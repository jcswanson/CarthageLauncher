package com.thesurix.gesturerecycler.transactions

/**
 * @author thesurix
 */
class AddTransaction<T>(
    private val item: T,
    private val headerEnabled: Boolean,
    private val transactional: Transactional<T> // Add a reference to the Transactional object
) : Transaction<T> {

    override fun perform(): Boolean {
        return transactional.data.apply {
            val success = add(item)
            if (success) {
                notifyInserted(if (headerEnabled) size else size - 1)
            }
            success
        }
    }

    override fun revert(): Boolean {
        return transactional.data.apply {
            val item = removeAt(size - 1)
            item?.let {
                notifyRemoved(if (headerEnabled) size + 1 else size)
                true
            } ?: false
        }
    }
}
