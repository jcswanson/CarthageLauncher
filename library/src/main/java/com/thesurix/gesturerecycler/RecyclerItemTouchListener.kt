// Package declaration for the GestureRecycler class
package com.thesurix.gesturerecycler

// Import necessary classes and interfaces
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

/**
 * Class that is responsible for handling item touch events.
 * Constructs [RecyclerView] touch listener.
 *
 * @param listener listener for item's click events
 * @author thesurix
 */
class RecyclerItemTouchListener<T>(listener: ItemClickListener<T>) : RecyclerView.SimpleOnItemTouchListener() {

    // Gesture detector for detecting touch events
    private var gestureDetector: GestureDetector? = null

    // Interface for handling click events on items
    interface ItemClickListener<T> {

        /**
         * Called when a tap occurs on a specified item.
         *
         * @param item pressed item
         * @param position item's position
         * @return true if the event is consumed, else false
         */
        fun onItemClick(item: T, position: Int): Boolean

        /**
         * Called when a long press occurs on a specified item.
         *
         * @param item pressed item
         * @param position item's position
         */
        fun onItemLongPress(item: T, position: Int)

        /**
         * Called when a double tap occurs on a specified item.
         *
         * @param item tapped item
         * @param position item's position
         * @return true if the event is consumed, else false
         */
        fun onDoubleTap(item: T, position: Int): Boolean
    }

    // Gesture click listener for handling touch events
    private val gestureClickListener = GestureClickListener(listener)

    // Override the onInterceptTouchEvent method to handle touch events
    override fun onInterceptTouchEvent(view: RecyclerView, e: MotionEvent): Boolean {

        // Get the child view under the touch event coordinates
        val childView = view.findChildViewUnder(e.x, e.y) ?: return false

        // Get the child view's position in the RecyclerView
        val childPosition = view.getChildAdapterPosition(childView)

        // Check if the child position is valid
        if (childPosition == RecyclerView.NO_POSITION) {
            return false
        }

        // Get the adapter for the RecyclerView
        val adapter = view.adapter

        // Check if the adapter is an instance of GestureAdapter
        if (adapter is GestureAdapter<*, *>) {
            val gestureAdapter = adapter as GestureAdapter<T, *>

            // Set the touched item and position for the gesture click listener
            gestureClickListener.setTouchedItem(gestureAdapter.getItem(childPosition), childPosition)
        }

        // Initialize the gesture detector if it is null
        if (gestureDetector == null) {
            gestureDetector = GestureDetector(view.context, gestureClickListener)
        }

        // Return the result of the gesture detector's onTouchEvent method
        return gestureDetector?.onTouchEvent(e) ?: false
    }
}

// Gesture click listener for handling touch events
private class GestureClickListener<T> internal constructor(private val listener: RecyclerItemTouchListener.ItemClickListener<T>)
    : GestureDetector.SimpleOnGestureListener() {

    // Variables to store the touched item and position
    private var item: T? = null
    private var viewPosition = 0

    // Override the onSingleTapConfirmed method to handle single tap events
    override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
        // Call the onItemClick method of the ItemClickListener interface
        return item?.let { listener.onItemClick(it, viewPosition) } ?: false
    }

    // Override the onLongPress method to handle long press events
    override fun onLongPress(e: MotionEvent) {
        // Call the onItemLongPress method of the ItemClickListener interface
        item?.let { listener.onItemLongPress(it, viewPosition) }
    }

    // Override the onDoubleTap method to handle double tap events
    override fun onDoubleTap(e: MotionEvent): Boolean {
        // Call the onDoubleTap method of the ItemClickListener interface
        return item?.let { listener.onDoubleTap(it, viewPosition) } ?: false
    }

    // Setter method for the touched item and position
    internal fun setTouchedItem(item: T, viewPosition: Int) {
        this.item = item
        this.viewPosition = viewPosition
    }
}
