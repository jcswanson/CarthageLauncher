package com.codesteem.mylauncher.gesture

import android.content.Context
import android.view.GestureDetector
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

/**
 * Class that is responsible for handling item touch events in a RecyclerView.
 * Constructs a touch listener for item clicks, long presses, and double taps.
 * @param listener listener for item's click events
 * @author thesurix
 */
class RecyclerItemTouchListener<T>(private val listener: ItemClickListener<T>) : RecyclerView.OnChildAttachStateChangeListener, View.OnTouchListener {

    private var gestureDetector: GestureDetector? = null
    private var gestureListener: GestureListener<T>? = null

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

    private inner class GestureListener<T>(private val listener: ItemClickListener<T>) : GestureDetector.SimpleOnGestureListener() {

        var item: T? = null
        var itemId: Long = RecyclerView.NO_ID

        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            return item?.let { listener.onItemClick(it, recyclerView.getChildAdapterPosition(itemView)) } ?: false
        }

        override fun onLongPress(e: MotionEvent) {
            item?.let { listener.onItemLongPress(it, recyclerView.getChildAdapterPosition(itemView)) }
            itemView.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            return item?.let { listener.onDoubleTap(it, recyclerView.getChildAdapterPosition(itemView)) } ?: false
        }
    }

    override fun onChildViewAttachedToWindow(view: View) {
        if (gestureListener == null) {
            gestureListener = GestureListener(listener)
            gestureDetector = GestureDetector(view.context, gestureListener)
            view.setOnTouchListener(this)
        }
    }

    override fun onChildViewDetachedFromWindow(view: View) {
        itemView.setOnTouchListener(null)
        gestureListener = null
        gestureDetector = null
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        return gestureDetector?.onTouchEvent(event) ?: false
    }
}
