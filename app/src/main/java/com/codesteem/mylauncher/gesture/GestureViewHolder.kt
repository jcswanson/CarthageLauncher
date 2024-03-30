package com.codesteem.mylauncher.gesture

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Base view holder class for gesture compatible items. This abstract class serves as the foundation 
 * for all view holders that need to support gesture functionality. It includes methods for 
 * managing the visibility of draggable and background views, as well as methods for binding data 
 * and handling item selection.
 *
 * @author thesurix
 */
abstract class GestureViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    /**
     * Returns view that can spawn drag gesture. If there is no view, simply return null. This method
     * is used to retrieve the view that can initiate a drag gesture. If there's no such view, return 
     * null.
     *
     * @return view that can spawn drag gesture
     */
    open val draggableView: View?
        get() = null

    /**
     * Returns top visible view (originally root view of the item). Override this method to use 
     * background view feature in case of swipe gestures. This method is used to retrieve the topmost
     * view of the item, which is initially the root view. You can override this method to implement
     * background view features for swipe gestures.
     *
     * @return top view
     */
    open val foregroundView: View
        get() = itemView

    /**
     * Returns background view which is visible when foreground view is partially or fully swiped. 
     * This method is used to retrieve the background view that becomes visible when the foreground view
     * is swiped either partially or completely.
     *
     * @return background view
     */
    open val backgroundView: View?
        get() = null

    /**
     * Method that shows view for manual drag gestures. Called only when getDraggableView() returns a 
     * valid view. This method is used to make the draggable view visible when manual drag gestures are
     * required.
     */
    fun showDraggableView() {
        draggableView?.visibility = View.VISIBLE
    }

    /**
     * Method that hides view for manual drag gestures. Called only when getDraggableView() returns a 
     * valid view. This method is used to hide the draggable view when manual drag gestures are not
     * required.
     */
    fun hideDraggableView() {
        draggableView?.visibility = View.GONE
    }

    /**
     * This method delegates bind logic into your ViewHolder. Implement the bind logic in your 
     * subclass to bind data to the views in the ViewHolder.
     *
     * @param item model from adapter's data collection
     * */
    open fun bind(item: T) {}

    /**
     * Indicates that ViewHolder is ready to recycle itself. Implement the recycling logic in your 
     * subclass to release any resources held by the ViewHolder.
     */
    open fun recycle() {}

    /**
     * Indicates that view is selected. Implement the selection logic in your subclass to update the
     * appearance of the view when it is selected.
     */
    open fun onItemSelect() {}

    /**
     * Indicates that view has no selection. Implement the deselection logic in your subclass to 
     * update the appearance of the view when it is no longer selected.
     */
    open fun onItemClear() {}

    open fun getBackgroundView(direction: Int): View? {
        return backgroundView
    }

    /**
     * Returns information if we can drag this view. Implement
