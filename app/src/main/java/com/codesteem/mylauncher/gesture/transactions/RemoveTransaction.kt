package com.codesteem.mylauncher.gesture.transactions

/**
 * The `RemoveTransaction` class is responsible for removing an item from a list and notifying the system
 * about the change. This class extends the `Transaction` interface and implements its two methods:
 * `perform` and `revert`.
 *
 * @param list The list from which the item will be removed.
 * @param position The position of the item to be removed.
 * @param headerEnabled A flag indicating whether the list has a header or not.
 * @author thesurix
 */
class RemoveTransaction<T>(private val list: MutableList<T>,
                           private val position: Int,
                           private val headerEnabled: Boolean) : Transaction<Unit> {

    // The item to be removed is stored here temporarily.
    private var item: T? = null

    /**
     * Performs the transaction by removing the item at the specified position from the list.
     */
    override fun perform() {
        item = list.removeAt(position)
    }

    /**
     * Reverts the transaction by adding the previously removed item back to the list.
     */
    override fun revert() {
        if (item != null && position >= 0 && position < list.size) {
            list.add(position, item!!)
        }
    }

    /**
     * Returns the position of the item that was removed.
     *
     * @return The position of the removed item.
     */
    fun getPosition(): Int {
        return position
    }

    /**
     * Returns the item that was removed.
     *
     * @return The removed item.
     */
    fun getItem(): T? {
        return item
    }
}
