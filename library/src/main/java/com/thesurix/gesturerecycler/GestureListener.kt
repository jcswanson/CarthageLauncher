package com.thesurix.gesturerecycler

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * The GestureListener class is a default gesture listener that handles manually spawned drag gestures.
 * It is designed to work with the ItemTouchHelper class from the Android Support Library.
 *
 * This class is intended to be used as a concrete implementation of the OnGestureListener interface
 * provided by the GestureAdapter class.
 *
 * @author thesurix
 * @see GestureAdapter
 * @see ItemTouchHelper
 */
class GestureListener(private val itemTouchHelper: ItemTouchHelper) :
    GestureAdapter.OnGestureListener<RecyclerView.ViewHolder> {

    /**
     * Called when a drag gesture is started on a view holder. This method will delegate the call
     * to the ItemTouchHelper's startDrag method, which will initiate the drag and drop operation.
     *
     * @param viewHolder The view holder that the drag gesture was started on.
     * @see ItemTouchHelper.Callback#startDrag(RecyclerView.ViewHolder)
     */
    override fun onStartDrag(viewHolder: GestureViewHolder<RecyclerView.ViewHolder>) {
        itemTouchHelper.startDrag(viewHolder.viewHolder)
    }
}
