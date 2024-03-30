package com.codesteem.mylauncher.util

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * A helper class for implementing touch events on RecyclerView items. This class provides swipe-to-delete functionality
 * and can also handle drag-and-drop events.
 *
 * @property adapter The adapter for the RecyclerView that this callback is associated with.
 */
class ItemTouchHelperCallback(private val adapter: ItemTouchHelperAdapter) : ItemTouchHelper.SimpleCallback(
    0, // Drag directions (not used for swipe-to-delete)
    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT // Swipe directions
) {
    /**
     * Called when an item is moved by the user. This method updates the adapter's data to reflect the new
     * position of the item.
     *
     * @param recyclerView The RecyclerView that the item is being moved in.
     * @param viewHolder The ViewHolder for the item being moved.
     * @param target The ViewHolder for the item that the first item is being moved to.
     * @return True if the move was successful, false otherwise.
     */
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        adapter.onItemMoved(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    /**
     * Called when an item is swiped by the user. This method updates the adapter's data to reflect the removal
     * of the item.
     *
     * @param viewHolder The ViewHolder for the item that was swiped.
     * @param direction The direction in which the item was swiped (either LEFT or RIGHT).
     */
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

