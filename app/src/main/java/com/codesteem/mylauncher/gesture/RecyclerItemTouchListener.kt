package com.codesteem.mylauncher.gesture

import android.view.GestureDetector
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

/**
 * Class that is responsible for handling item touch events in a RecyclerView.
 * Constructs a touch listener for item clicks, long presses, and double taps.
 * @param listener listener for item's click events
 * @author thesurix
 */
class RecyclerItemTouchListener<T>(private val listener: ItemClickListener<T>) : RecyclerView.SimpleOnItemTouchListener() {

    private var gestureDetector: GestureDetector? = null

    /**
     * The listener that is used to notify when a tap, long press or double tap occur.
     */
    interface ItemClickListener<T> {

        /**
         * Called when a tap occurs on a specified item.
         * @param item pressed item
         * @param position item's position
         * @return true if the event is consumed, else false
         */
        fun onItemClick(item: T, position: Int): Boolean

        /**
         * Called when a long press occurs on a specified item.
         * @param item pressed item
         * @param position item's position
         */
        fun onItemLongPress(item: T, position: Int)

        /**
         * Called when a double tap occurs on a specified item.
         * @param item tapped item
         * @param position item's position
         * @return true if the event is consumed, else false
         */
        fun onDoubleTap(item: T, position: Int): Boolean
    }

    private val gestureClickListener = GestureClickListener(listener)

    /**
     * Override the onInterceptTouchEvent method to detect touch events on the RecyclerView.
     * Find the child view under the touch event coordinates and get its position.
     * If the position is valid and the adapter is an instance of GestureAdapter,
     * set the touched item and position for the gesture listener.
     * Use the gesture detector to handle touch events and return the result.
     */
    override fun onInterceptTouchEvent(view: RecyclerView, e: MotionEvent): Boolean {
        val childView = view.findChildViewUnder(e.x, e.y) ?: return false
        val childPosition = view.getChildAdapterPosition(childView)
        if (childPosition == RecyclerView.NO_POSITION) {
            return false
        }

        val adapter = view.adapter
        if (adapter is GestureAdapter<*, *>) {
            val gestureAdapter = adapter as GestureAdapter<T, *>
            gestureClickListener.setTouchedItem(gestureAdapter.getItem(childPosition), childPosition)
        }

        if (gestureDetector == null) {
            gestureDetector = GestureDetector(view.context, gestureClickListener)
        }

        return gestureDetector?.onTouchEvent(e) ?: false
    }
}

private class GestureClickListener<T> internal constructor(private val listener: RecyclerItemTouchListener.ItemClickListener<T>)
    : GestureDetector.SimpleOnGestureListener() {

    private var item: T? = null
    private var viewPosition = 0

    /**
     * Called when a single tap is confirmed.
     * If the touched item and position are set, call the onItemClick method of the listener.
     */
    override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
        return item?.let { listener.onItemClick(it, viewPosition) } ?: false
    }

    /**
     * Called when a long press is detected.
     * If the touched item and position are set, call the onItemLongPress method of the listener.
     */
    override fun onLongPress(e: MotionEvent) {
        item?.let { listener.onItemLongPress(it, viewPosition) }
    }

    /**
     * Called when a double tap is detected.
     * If the touched item and position are set, call the onDoubleTap method of the listener.
     */
    override fun onDoubleTap(e: MotionEvent): Boolean {
        return item?.let { listener.onDoubleTap(it, viewPosition) } ?: false
    }

    /**
     * Set the touched item and position for the gesture listener.
     */
    internal fun setTouchedItem(item: T, viewPosition: Int) {
        this.item = item
        this.viewPosition = viewPosition
    }
}
