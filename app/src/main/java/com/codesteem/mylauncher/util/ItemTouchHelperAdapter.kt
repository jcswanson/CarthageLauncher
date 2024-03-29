package com.codesteem.mylauncher.util

import androidx.recyclerview.widget.RecyclerView

interface ItemTouchHelperAdapter {
    fun onItemSwiped(position: Int, direction: Int, viewHolder: RecyclerView.ViewHolder)
    fun onItemMoved(fromPosition: Int, toPosition: Int)
}
