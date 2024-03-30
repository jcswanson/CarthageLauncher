package com.codesteem.mylauncher.adapter

import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.codesteem.mylauncher.test.MonthsAdapter

/**
 * A custom [ItemTouchHelper.Callback] implementation for the [MonthsAdapter] to handle drag and swipe gestures.
 *
 * @property adapter The [MonthsAdapter] instance to interact with.
 */
class ItemTouchHelperCallback(
    private val adapter: MonthsAdapter
) : ItemTouchHelper.Callback() {

    /**
     * Returns the movement flags for the given [recyclerView] and [viewHolder]. In this case,
     * allowing both dragging and swiping in all directions.
     */
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return ItemTouchHelper.Callback.makeMovementFlags(
            ItemTouchHelper.ACTION_STATE_DRAG, // Allowing dragging
            ItemTouchHelper.ACTION_STATE_SWIPE // Allowing swiping
        )
    }

    /**
     * Called when an item is moved in the [recyclerView]. This method updates the adapter's data and
     * notifies the adapter of the change.
     *
     * @param recyclerView The [RecyclerView] instance.
     * @param viewHolder   The [RecyclerView.ViewHolder] instance representing the source item.
     * @param target       The [RecyclerView.ViewHolder] instance representing the target item.
     * @return True if the move is successful, false otherwise.
     */
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        Log.e("onMove", "onMove")
        adapter.move(viewHolder.adapterPosition, target.adapterPosition)

