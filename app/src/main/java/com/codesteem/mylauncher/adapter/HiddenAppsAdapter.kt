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

class HiddenAppsAdapter(
    @param:LayoutRes private val mItemResId: Int
): GestureAdapter<MonthItem, GestureViewHolder<MonthItem>>() {

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

    override fun getItemViewType(viewPosition: Int): Int {
        val handledType = super.getItemViewType(viewPosition)
        if (handledType > 0) {
            return handledType
        }
        return getItemByViewPosition(viewPosition).type.ordinal
    }

    private fun createHeaderOrFooterViewHolder(context: Context, parent: ViewGroup, @LayoutRes layout: Int): GestureViewHolder<MonthItem> {
        val view = LayoutInflater.from(context).inflate(layout, parent, false)
        return object : GestureViewHolder<MonthItem>(view) {
            override fun canDrag() = false

            override fun canSwipe() = false
        }
    }

    fun addItem(selectedApp: MonthItem) {
        val list = ArrayList(data)
        list.add(selectedApp)
        notifyDataSetChanged()
    }

    private fun removeItem(selectedApp: MonthItem) {
        val list = ArrayList(data)
        list.remove(selectedApp)
        notifyDataSetChanged()
    }

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
