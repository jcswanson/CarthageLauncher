package com.codesteem.mylauncher.gesture.transactions

import com.codesteem.mylauncher.gesture.util.swap

/**
 * @author thesurix
 */
class SwapTransaction<T>(private val firstIndex: Int,
                         private val secondIndex: Int,
                         private val headerEnabled: Boolean) : Transaction<T> {

    override fun perform(transactional: Transactional<T>): Boolean {
        transactional.data.swap(firstIndex, secondIndex)
        notifyChanged(transactional)
        return true
    }

    override fun revert(transactional: Transactional<T>): Boolean {
        transactional.data.swap(secondIndex, firstIndex)
        notifyChanged(transactional)
        return true
    }

    private fun notifyChanged(transactional: Transactional<T>) {
        val changedOffset = + if(headerEnabled) 1 else 0
        transactional.notifyChanged(firstIndex + changedOffset)
        transactional.notifyChanged(secondIndex + changedOffset)
    }
}