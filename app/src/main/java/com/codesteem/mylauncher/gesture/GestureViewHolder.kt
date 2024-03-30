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

    abstract val draggableView: View?
    abstract val foregroundView: View
    abstract val backgroundView: View?

    init {
        require(draggableView == null || draggableView === itemView || itemView.parent == draggableView) {
            "Draggable view should be a child of the item view"
        }
    }

    fun showDraggableView() {
        draggableView?.visibility = View.VISIBLE
    }

    fun hideDraggableView() {
        draggableView?.visibility = View.GONE
    }

    abstract fun bind(item: T)

    open fun recycle() {}

    open fun onItemSelect() {}

    open fun onItemClear() {}

    open fun getBackgroundView(direction: Int): View? {
        return backgroundView
    }

    open fun canDrag(): Boolean = draggableView != null
}
