package com.codesteem.mylauncher.gesture.transactions

/**
 * The `RemoveTransaction` class is responsible for removing an item from a list and notifying the system
 * about the change. This class extends the `Transaction` interface and implements its two methods:
 * `perform` and `revert`.
 *
 * @param position The position of the item to be removed.
 * @param headerEnabled A flag indicating whether the list has a header or not.
 * @author thesurix
 */
class RemoveTransaction<T>(private val position: Int,
                           private val headerEnabled: Boolean) : Transaction<T> {

    // The item to be removed is stored here temporarily.
    private var item: T? = null

    /**

