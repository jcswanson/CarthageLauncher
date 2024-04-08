package com.codesteem.mylauncher.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.codesteem.mylauncher.R
import com.codesteem.mylauncher.databinding.GridItemBinding
import com.codesteem.mylauncher.databinding.MonthHeaderItemBinding
import com.codesteem.mylauncher.gesture.GestureAdapter
import com.codesteem.mylauncher.gesture.GestureViewHolder
import com.codesteem.mylauncher.gesture.TYPE_HEADER_ITEM
import com.codesteem.mylauncher.test.GridItemViewHolder
import com.codesteem.mylauncher.test.MonthHeaderViewHolder
import com.codesteem.mylauncher.test.MonthItem

/**
 * A custom adapter for handling the display of hidden apps in a grid layout.
 * This adapter is responsible for creating and managing the view holders for each item in the grid.
 */
class HiddenAppsAdapter(
    @param:LayoutRes private val mItemResId: Int // The layout resource ID for the grid items
) : GestureAdapter<MonthItem, GestureViewHolder<MonthItem>>() {

    /**
     * Creates a new view holder for the given parent and view type.
     * This method determines the type of the view holder based on the provided view type and inflates
     * the corresponding layout resource.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GestureViewHolder<MonthItem> {
        return if (viewType == MonthItem.MonthItemType.MONTH.ordinal) {
            when (mItemResId) {
                R.layout.grid_item -> GridItemViewHolder(
                    GridItemBinding.inflate(LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
                else -> throw UnsupportedOperationException("Unsupported resource")
            }
        } else if (viewType == TYPE_HEADER_ITEM) {
            createHeaderOrFooterViewHolder(parent.context, parent, R.layout.header_item)
        } else {
            MonthHeaderViewHolder(MonthHeaderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    /**
     * Returns the view type for the item at the specified position.
     * This method first calls the superclass implementation to handle any already assigned view types,
     * and if none is found, it returns the view type based on the item's type.
     */
    override fun getItemViewType(viewPosition: Int): Int {
        val handledType = super.getItemViewType(viewPosition)
        if (handledType > 0) {
            return handledType
        }
        return getItemByViewPosition(viewPosition).type.ordinal
    }

    /**
     * Creates a header or footer view holder for the given context, parent, and layout resource.
     * This method inflates the given layout and returns a new view holder with the inflated view.
     */
    private fun createHeaderOrFooterViewHolder(context: Context, parent: ViewGroup, @LayoutRes layout: Int): GestureViewHolder<MonthItem> {
        val view = LayoutInflater.from(context).inflate(layout, parent, false)
        return object : GestureViewHolder<MonthItem>(view) {
            override fun canDrag() = false // Disables dragging for header/footer view holders
            override fun canSwipe() = false // Disables swiping for header/footer view holders
        }
    }

    /**
     * Adds a new item to the adapter's data list and notifies the adapter to redraw the items.
     */
    fun addItem(selectedApp: MonthItem) {
        val list = ArrayList(data) // Creates a mutable copy of the data list
        list.add(selectedApp)
        notifyDataSetChanged() // Notifies the adapter to redraw the items
    }

    /**
     * Removes an item from the adapter's data list and notifies the adapter to redraw the items.
     */
    private fun removeItem(selectedApp: MonthItem) {
        val list = ArrayList(data) // Creates a mutable copy of the data list
        list.remove(selectedApp)
        notifyDataSetChanged() // Notifies the adapter to redraw the items
    }

    /**
     * Moves an item from one position to another within the adapter's data list and notifies the
     * adapter to redraw the items.
     */
    fun moveItem(from: Int, to: Int) {
        val list = data.toMutableList() // Creates a mutable copy of
