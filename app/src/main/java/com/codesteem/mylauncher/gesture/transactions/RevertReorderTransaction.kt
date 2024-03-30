package com.codesteem.mylauncher.gesture.transactions

/**
 * A transaction to revert a reorder of items in a list.
 *
 * @author thesurix
 */
class RevertReorderTransaction<T>(
    private val from: Int,
    private val to: Int,
    private val headerEnabled: Boolean,
    private val transactional: Transactional<T>
) : Transaction<T> {

    override fun perform(transactional: Transactional<T>): Boolean {
        return revert(transactional)
    }

    override fun revert(transactional: Transactional<T>): Boolean {
        return with(transactional.data) {
            val item = removeAt(to)
            item?.let {
                val adjustedFrom = from + if (headerEnabled) 1 else 0
                val adjustedTo = to + if (headerEnabled) 1 else 0
                transactional.notifyRemoved(adjustedTo)
                add(adjustedFrom, it)
                transactional.notifyInserted(adjustedFrom)
                true
            } ?: false
        }
    }
}
