package com.codesteem.mylauncher.adapter

import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.codesteem.mylauncher.test.MonthsAdapter

class ItemTouchHelperCallback(
    private val adapter: MonthsAdapter
) : ItemTouchHelper.Callback() {

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        // Allow dragging and swiping in all directions
        return ItemTouchHelper.Callback.makeMovementFlags(
            ItemTouchHelper.ACTION_STATE_DRAG,
            ItemTouchHelper.ACTION_STATE_SWIPE
        )
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        Log.e("onMove", "onMove")
        adapter.move(viewHolder.adapterPosition, target.adapterPosition)
        adapter.notifyItemMoved(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        Log.e("onSwipe", "onSwipe")
    }
}