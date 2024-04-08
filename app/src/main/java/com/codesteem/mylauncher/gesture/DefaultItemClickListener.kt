package com.codesteem.mylauncher.gesture

/**
 * This class provides a default implementation of the [RecyclerItemTouchListener.ItemClickListener] interface.
 * It defines the behavior for handling click, long press, and double tap events on items in a RecyclerView.
 *
 * @author thesurix
 * @param <T> The type of the item being clicked.
 */
open class DefaultItemClickListener<T> : RecyclerItemTouchListener.ItemClickListener<T> {

    /**
     * This method is called when an item in the RecyclerView is clicked. By default, it returns false,
     * indicating that the click event was not handled.
     *
     * @param item The item that was clicked.
     * @param position The position of the item in the RecyclerView.
     * @return true if the click event was handled, false otherwise.
     */
    override fun onItemClick(item: T, position: Int): Boolean {
        return false
    }

    /**

