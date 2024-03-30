package com.codesteem.mylauncher.gesture

/**
 * Default implementation of the [RecyclerItemTouchListener.ItemClickListener] interface. This class provides basic functionality for handling click events on items in a RecyclerView.
 *
 * @author thesurix
 * @param <T> The type of data being displayed in the RecyclerView.
 */
open class DefaultItemClickListener<T> : RecyclerItemTouchListener.ItemClickListener<T> {

    /**
     * Called when an item in the RecyclerView is clicked. By default, this method returns false, indicating that the click was not handled.
     *
     * @param item The data associated with the clicked item.
     * @param position The position of the clicked item in the RecyclerView.
     * @return True if the click was handled, false otherwise.
     */
    override fun onItemClick(item: T, position: Int): Boolean {
        return false
    }

    /**
     * Called when an item in the RecyclerView is long-pressed. By default, this method does nothing.
     *
     * @param item The data associated with the long-pressed item.
     * @param position The position of the long-pressed item in the RecyclerView.
     */
    override fun onItemLongPress(item: T, position: Int) {
    }

    /**
     * Called when an item in the RecyclerView is double-tapped. By default, this method returns false, indicating that the double-tap was not handled.
     *
     * @param item The data associated with the double-tapped item.
     * @param position The position of the double-tapped item in the RecyclerView.
     * @return True if the double-tap was handled, false otherwise.
     */
    override fun onDoubleTap(item: T, position: Int): Boolean {
        return false
    }
}
