package com.codesteem.mylauncher.gesture

/**
 * Default implementation of the [RecyclerItemTouchListener.ItemClickListener] interface. This class provides basic functionality for handling click events on items in a RecyclerView.
 *
 * @author thesurix
 * @param T The type of data being displayed in the RecyclerView.
 */
interface DefaultItemClickListener<T> : RecyclerItemTouchListener.ItemClickListener<T> {

    /**
     * Called when an item in the RecyclerView is clicked. By default, this method does nothing.
     *
     * @param item The data associated with the clicked item.
     * @param position The position of the clicked item in the RecyclerView.
     */
    override fun onItemClick(item: T, position: Int) {
        // Can be overridden by implementations to handle click events.
    }

    /**
     * Called when an item in the RecyclerView is long-pressed. By default, this method does nothing.
     *
     * @param item The data associated with the long-pressed item.
     * @param position The position of the long-pressed item in the RecyclerView.
     */
    override fun onItemLongPress(item: T, position: Int) {
        // Can be overridden by implementations to handle long-press events.
    }

    /**
     * Called when an item in the RecyclerView is double-tapped. By default, this method does nothing.
     *
     * @param item The data associated with the double-tapped item.
     * @param position The position of the double-tapped item in the RecyclerView.
     */
    override fun onDoubleTap(item: T, position: Int) {
        // Can be overridden by implementations to handle double-tap events.
    }
}
