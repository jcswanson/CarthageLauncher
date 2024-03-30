package com.codesteem.mylauncher.gesture

import androidx.recyclerview.widget.ItemTouchHelper

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
class GestureListener(private val touchHelper: ItemTouchHelper) : GestureAdapter.OnGestureListener<Any> {

    /**
     * Notifies the [ItemTouchHelper] to start tracking the drag movement of the given [viewHolder].
     *
     * This method is called when a drag gesture is first detected. It is responsible for initiating
     * the drag movement and allowing the user to move the [viewHolder] to a new position in the
     * RecyclerView.
     *
     * @param viewHolder The [GestureViewHolder] that is being dragged.
     */
    override fun onStartDrag(viewHolder: GestureViewHolder<Any>) {
        touchHelper.startDrag(viewHolder)
    }

    /**
     * Notifies the [ItemTouchHelper] to stop tracking the drag movement of the given [viewHolder].
     *
     * This method is called when the user has finished dragging the [viewHolder] to its new position.
     * It is responsible for ending the drag movement and updating the RecyclerView with the new
     * position of the [viewHolder].
     *
     * @param viewHolder The [GestureViewHolder] that has been dragged to its new position.
     */
