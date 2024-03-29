package com.codesteem.mylauncher.gesture.transactions

/**
 * @author thesurix
 */
interface Transaction<T> {
    fun perform(transactional: Transactional<T>): Boolean
    fun revert(transactional: Transactional<T>): Boolean
}