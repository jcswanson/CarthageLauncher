package com.codesteem.mylauncher.gesture.transactions

/**
 * @author thesurix
 */
interface Transactional<T> {
    val data: MutableList<T>
    fun notifyChanged(position: Int)
    fun notifyInserted(position: Int)
    fun notifyRemoved(position: Int)
    fun notifyMoved(fromPosition: Int, toPosition: Int)
}