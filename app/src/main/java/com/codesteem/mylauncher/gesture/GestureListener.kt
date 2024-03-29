package com.codesteem.mylauncher.gesture


import androidx.recyclerview.widget.ItemTouchHelper

/**
 * Default gesture listener that handles manual spawned drag gestures.
 * @author thesurix
 */
class GestureListener(private val touchHelper: ItemTouchHelper) : GestureAdapter.OnGestureListener<Any> {

    override fun onStartDrag(viewHolder: GestureViewHolder<Any>) {
        touchHelper.startDrag(viewHolder)
    }

    override fun onEndDrag(viewHolder: GestureViewHolder<Any>) {
        touchHelper.startDrag(viewHolder)
    }
}
