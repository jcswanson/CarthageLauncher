package com.thesurix.gesturerecycler.transactions

/**
 * Marker interface for an object that supports transactions.
 * A transaction represents a single unit of work that can be executed and then undone,
 * leaving the system in the same state as before the transaction.
 *
 * @author thesurix
 */
interface Transactional<T>

/**
 * Interface for a transaction that can be performed and reverted on a [Transactional] object.
 *
 * @param T The type of the object on which the transaction will be performed.
 * @author thesurix
 */
interface Transaction<T> {
    /**
     * Performs the transaction on the given [transactional] object.
     * Returns true if the transaction was performed successfully, false otherwise.
     *
     * @param transactional The object on which the transaction will be performed.
     * @return True if the transaction was performed successfully, false otherwise.
     */
    fun perform(transactional: Transactional<T>): Boolean

    /**
     * Reverts the transaction on the given [transactional] object.
     * Returns true if the transaction was reverted successfully, false otherwise.
     *
     * @param transactional The object on which the transaction will be reverted.
     * @return True if the transaction was reverted successfully, false otherwise.
     */
    fun revert(transactional: Transactional<T>): Boolean
}
