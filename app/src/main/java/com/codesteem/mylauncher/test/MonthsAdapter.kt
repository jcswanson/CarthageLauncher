package com.codesteem.mylauncher.test

import android.content.Context
import android.view.LayoutInflater
import androidx.annotation.LayoutRes
import com.codesteem.mylauncher.R
import com.codesteem.mylauncher.databinding.GridItemBinding
import com.codesteem.mylauncher.databinding.MonthHeaderItemBinding
import com.codesteem.mylauncher.gesture.GestureAdapter
import com.codesteem.mylauncher.gesture.GestureViewHolder
import com.codesteem.mylauncher.gesture.TYPE_HEADER_ITEM

/**
 * Adapter for displaying months in a grid layout.
 * This adapter handles both regular month items and header items.
 */
class MonthsAdapter(
    @param:LayoutRes private val mItemResId: Int
) : GestureAdapter<MonthItem, GestureViewHolder<MonthItem>>() {

    /**
     * Creates a ViewHolder for the given item type and view.
     * This method is called by the RecyclerView to display each item in the data set.
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
     * This method is used to determine the type of ViewHolder to use for the item.
     */
    override fun getItemViewType(viewPosition: Int): Int {
        val handledType = super.getItemViewType(viewPosition)
        if (handledType > 0) {
            return handledType
        }
        return getItemByViewPosition(viewPosition).type.ordinal
    }

    /**
     * Creates a ViewHolder for a header or footer item.
     * This method is used to create ViewHolders for header and footer items.
     */
    private fun createHeaderOrFooterViewHolder(context: Context, parent: ViewGroup, @LayoutRes layout: Int): GestureViewHolder<MonthItem> {
        val view = LayoutInflater.from(context).inflate(layout, parent, false)
        return object : GestureViewHolder<MonthItem>(view) {
            override fun canDrag() = false

            override fun canSwipe() = false
        }
    }

    /**
     * Adds a new month item to the adapter.
     * This method is used to add a new month item to the adapter.
     */
    fun addItem(selectedApp: MonthItem) {
        val list = ArrayList(data)
        list.add(selectedApp)
        notifyDataSetChanged()
    }

    /**
     * Removes a month item from the adapter.
     * This method is used to remove a month item from the adapter.
     */
    private fun removeItem(selectedApp: MonthItem) {
        val list = ArrayList(data)
        list.remove(selectedApp)
        notifyDataSetChanged()
    }

    /**
     * Moves a month item to a new position in the adapter.
     * This method is used to move a month item to a new position in the adapter.
     */
    fun moveItem(from: Int, to: Int) {
        val list = data.toMutableList()
        val fromLocation = list[from]
        list.removeAt(from)
        if (to < from) {
            list.add(to + 1 , fromLocation)
        } else {
            list.add(to - 1, fromLocation)
        }
        data = list
    }
}
