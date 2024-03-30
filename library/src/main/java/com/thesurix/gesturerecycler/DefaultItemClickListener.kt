package com.thesurix.gesturerecycler

/**
 * This class provides a default implementation of the [RecyclerItemTouchListener.ItemClickListener] interface.
 * It can be used to handle click and long press events for items in a RecyclerView.
 *
 * @author thesurix
 * @param <T> The type of data being displayed in the RecyclerView.
 */
open class DefaultItemClickListener<T> : RecyclerItemTouchListener.ItemClickListener<T> {

    /**
     * This method is called when an item in the RecyclerView is clicked.
     * By default, this method does nothing.
     * Override this method to implement custom behavior when an item is clicked.
     *
     * @param item The data associated with the clicked item.
     * @param position The position of the clicked item in the RecyclerView.
     */
    override fun onItemClick(item: T, position: Int) {
        // Do nothing by default
    }

    /**
     * This method is called when an item in the RecyclerView is long pressed.
     * By default, this method does nothing.
     * Override this method to implement custom behavior when an item is long pressed.
     *
     * @param item The data associated with the long pressed item.
     * @param position The position of the long pressed item in the RecyclerView.
     */
    override fun onItemLongPress(item: T, position: Int) {
        // Do nothing by default
    }

    /**
     * This method is called when an item in the RecyclerView is double tapped.
     * By default, this method returns false, indicating that the double tap was not handled.
     * Override this method to implement custom behavior when an item is double tapped.
     *
     * @param item The data associated with the double tapped item.
    
