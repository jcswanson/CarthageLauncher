package com.thesurix.gesturerecycler

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Base view holder class for gesture compatible items.
 * This class provides basic functionality for handling gestures in RecyclerView items.
 * It includes methods for showing and hiding draggable views, binding data to the view holder,
 * recycling the view holder, and handling item selection.
 * @author thesurix
 */
abstract class GestureViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    /**
     * Returns view that can spawn drag gesture. If there is no view simply return null.
     * This method is used to get the view that can be dragged when handling drag gestures.
     * By default, it returns null, indicating that there is no draggable view.
     * @return view that can spawn drag gesture
     */
    open val draggableView: View?
        get() = null

    /**
     * Returns top visible view (originally root view of the item),
     * override this method to use background view feature in case of swipe gestures.
     * This method is used to get the top visible view of the item.
     * By default, it returns the itemView, which is the root view of the item.
     * @return top view
     */
    open val foregroundView: View
        get() = itemView

    /**
     * Returns background view which is visible when foreground view is partially or fully swiped.
     * This method is used to get the background view of the item, which is visible when the foreground view is swiped.
     * By default, it returns null, indicating that there is no background view.
     * @return background view
     */
    open val backgroundView: View?
        get() = null

    /**
     * Method that shows view for manual drag gestures.
     * Called only when getDraggableView() returns valid view.
     * This method is used to show the draggable view when handling drag gestures.
     * It is called only when getDraggableView() returns a valid view.
     */
    fun showDraggableView() {
        draggableView?.visibility = View.VISIBLE
    }

    /**
     * Method that hides view for manual drag gestures.
     * Called only when getDraggableView() returns valid view.
     * This method is used to hide the draggable view when handling drag gestures.
     * It is called only when getDraggableView() returns a valid view.
     */
    fun hideDraggableView() {
        draggableView?.visibility = View.GONE
    }

    /**
     * This method delegates bind logic into your ViewHolder.
     * @param item model from adapter's data collection
     * This method is used to bind data to the view holder.
     * It takes an item as a parameter, which is the model from the adapter's data collection.
     * By default, it does not do anything, and it should be overridden in the subclass to implement the bind logic.
     * */
    open fun bind(item: T) {}

    /**
     * Indicates that ViewHolder is ready to recycle itself.
     * This method is called when the view holder is ready to be recycled.
     * It can be overridden in the subclass to perform any cleanup or preparation for recycling.
     * By default, it does not do anything.
     */
    open fun recycle() {}

    /**
     * Indicates that view is selected.
     * This method is called when the view is selected.
     * It can be overridden in the subclass to implement the selection logic.
