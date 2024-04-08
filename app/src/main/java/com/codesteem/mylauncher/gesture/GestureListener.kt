package com.codesteem.mylauncher.gesture

import androidx.recyclerview.widget.ItemTouchHelper

/**
 * A gesture listener that handles manually spawned drag gestures. This class is intended to be used with
 * the [GestureAdapter] to enable drag and drop functionality in a RecyclerView.
 *
 * When a drag gesture is started by calling [.onStartDrag], the [ItemTouchHelper] associated with
 * this listener will begin tracking the drag movement. The drag movement can then be ended by
 * calling [.onEndDrag].
 *
 * This class is useful when you want to provide users with the ability to manually rearrange items in
 * a RecyclerView using drag and drop gestures.
 *
 * @author thesurix
 */
class GestureListener(private val touchHelper: ItemTouchHelper) : GestureAdapter.OnGestureListener<Any> {

    /**
     * Starts a drag gesture for the given view holder. This method should be called when the user
     * initiates a drag gesture, such as by pressing and holding an item in the RecyclerView.
     *
     * Once this method is called, the [ItemTouchHelper] associated with this listener will begin
     * tracking the drag movement.
     *
     * @param viewHolder The view holder representing the item being dragged.
     */
    override fun onStartDrag(viewHolder: GestureViewHolder<Any>) {
        touchHelper.startDrag(viewHolder)
    }

    /**
     * Ends a drag gesture for the given view holder. This method should be called when the user has
     * finished dragging an item in the RecyclerView.
     *
     * Once this method is called, the [ItemTouchHelper] associated with this listener will stop
     * tracking the drag movement.
     *
     * @param viewHolder The view holder representing the item being dragged.
     */
    override fun onEndDrag(viewHolder: GestureViewHolder<Any>) {
        touchHelper.startDrag(viewHolder)
   
