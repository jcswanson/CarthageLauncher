package com.codesteem.mylauncher.gesture

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * A gesture listener that handles manually spawned drag gestures. This class is intended to be used with
 * the [GestureAdapter] to enable drag and drop functionality in a RecyclerView.
 *
 * When a drag gesture is started, this listener will notify the [ItemTouchHelper] to begin tracking
 * the drag movement. Once the drag gesture has ended, this listener will again notify the
 * [ItemTouchHelper] to stop tracking the movement.
 *
 * This implementation is designed to be simple and easy to use. Simply create an instance of this
 * class and pass it to your [GestureAdapter] to enable drag and drop functionality.
 *
 * @author thesurix
 */
class GestureListener(private val touchHelper: ItemTouchHelper) :
    RecyclerView.OnItemTouchListener,
    GestureAdapter.OnGestureListener<Any> {

    private var draggingViewHolder: RecyclerView.ViewHolder? = null

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        if (draggingViewHolder == null) {
            val viewHolder = rv.findChildViewUnder(e.x, e.y)
            if (viewHolder is GestureViewHolder<Any> && shouldInterceptTouchEvent(e)) {
                touchHelper.startDrag(viewHolder)
                draggingViewHolder = viewHolder
                return true
            }
        }
        return false
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
        if (draggingViewHolder != null) {
            touchHelper.processDrag(draggingViewHolder!!, rv, e)
            if (!e.isActionInProgress) {
                draggingViewHolder = null
            }
        }
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

    override fun onStartDrag(viewHolder: GestureViewHolder<Any>) {
        draggingViewHolder = viewHolder
    }

    override fun onStopDrag(viewHolder: GestureViewHolder<Any>) {
        draggingViewHolder = null
    }

    private fun shouldInterceptTouchEvent(e: MotionEvent): Boolean {
        // Implement your logic to determine if the touch event should be intercepted.
        // For example, you may want to check if the user has long-pressed on an item.
        return true
    }
}
