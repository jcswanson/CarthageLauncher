// Package declaration for the GestureRecycler class
package com.thesurix.gesturerecycler

// Import necessary classes and interfaces
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

/**
 * The RecyclerItemTouchListener class is responsible for handling touch events on RecyclerView items.
 * It constructs a touch listener for RecyclerView.
 *
 * @param listener The ItemClickListener that will be notified when tap, long press, or double tap events occur.
 * @author thesurix
 */
class RecyclerItemTouchListener<T>(private val listener: ItemClickListener<T>) : RecyclerView.OnItemTouchListener {

    // Interface for listeners interested in item click events
    interface ItemClickListener<T> {

        // Called when a tap occurs on a specified item
        fun onItemClick(item: T, position: Int): Boolean

        // Called when a long press occurs on a specified item
        fun onItemLongPress(item: T, position: Int)

        // Called when a double tap occurs on a specified item
        fun onDoubleTap(item: T, position: Int): Boolean
    }

    // GestureClickListener handles gesture events and maps them to corresponding item click events
    private inner class GestureClickListener(private val itemClickListener: ItemClickListener<T>) : GestureDetector.SimpleOnGestureListener() {

        // The currently touched item and its position
        private var item: T? = null
        private var viewPosition = 0

        // Sets the touched item and its position
        internal fun setTouchedItem(item: T, viewPosition: Int) {
            this.item = item
            this.viewPosition = viewPosition
        }

        // Called when a single tap is confirmed
        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            return item?.let { itemClickListener.onItemClick(it, viewPosition) } ?: false
        }

        // Called when a long press is detected
        override fun onLongPress(e: MotionEvent) {
            item?.let { itemClickListener.onItemLongPress(it, viewPosition) }
        }

        // Called when a double tap is detected
        override fun onDoubleTap(e: MotionEvent): Boolean {
            return item?.let { itemClickListener.onDoubleTap(it, viewPosition) } ?: false
        }
    }

    // GestureDetector for detecting gestures
    private var gestureDetector: GestureDetector? = null

    // Overridden method called when a touch event is detected
    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {

        // Get the child view under the touch event coordinates
        val childView = rv.findChildViewUnder(e.x, e.y) ?: return false

        // Get the child view's position in the RecyclerView
        val childPosition = rv.getChildAdapterPosition(childView)

        // Return if the child position is not valid
        if (childPosition == RecyclerView.NO_POSITION) {
            return false
        }

        // Get the adapter for the RecyclerView
        val adapter = rv.adapter

        // Check if the adapter is an instance of GestureAdapter
        if (adapter is GestureAdapter<*, *>) {
            val gestureAdapter = adapter as GestureAdapter<T, *>

            // Set the touched item and its position
            gestureClickListener.setTouchedItem(gestureAdapter.getItem(childPosition), childPosition)
        }

        // Initialize the GestureDetector if it's null
        if (gestureDetector == null) {
            gestureDetector = GestureDetector(rv.context, gestureClickListener)
        }

        // Return whether the GestureDetector detected a gesture
        return gestureDetector?.onTouchEvent(e) ?: false
    }

    // Overridden method called when a touch event is released
    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
        // Do nothing
    }

    // Overridden method called when a touch event is released
    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        // Do nothing
    }
}
