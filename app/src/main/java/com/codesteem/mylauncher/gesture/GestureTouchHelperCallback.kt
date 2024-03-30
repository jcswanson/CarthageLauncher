package com.codesteem.mylauncher.gesture

import android.graphics.Canvas
import android.view.View
import androidx.recyclerview.widget.*
import com.codesteem.mylauncher.gesture.LayoutFlags.*

/**
 * Touch helper callback that handles different RecycleView gestures.
 * Constructs callback object based on passed adapter.
 *
 * @param adapter adapter
 * @author thesurix
 */
private val DIRECTIONS = listOf(ItemTouchHelper.LEFT, ItemTouchHelper.RIGHT, ItemTouchHelper.UP, ItemTouchHelper.DOWN)
class GestureTouchHelperCallback(private val gestureAdapter: GestureAdapter<*, *>) : ItemTouchHelper.Callback() {

    /**
     * Flag that enables or disables swipe gesture 
     */
    var swipeEnabled = false

    /**
     * Flag that enables or disables manual drag gesture 
     */
    var manualDragEnabled = false
        set(enabled) {
            field = enabled
            gestureAdapter.allowManualDrag(manualDragEnabled) // Allows or disallows manual dragging for the adapter
        }

    /**
     * Flag that enables long press drag gesture 
     */
    var longPressDragEnabled = false

    /**
     * Flags for drag gesture 
     */
    var dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN

    /**
     * Flags for swipe gesture 
     */
    var swipeFlags = ItemTouchHelper.RIGHT

    /**
     * Determines the movement flags based on the view holder's drag and swipe capabilities.
     *
     * @param recyclerView The RecyclerView containing the view holder.
     * @param viewHolder   The view holder to get the flags for.
     * @return The movement flags.
     */
    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val holder = viewHolder as GestureViewHolder<*>
        return makeMovementFlags(
            if (holder.canDrag()) dragFlags else 0, // If the holder can drag, use the drag flags; otherwise, use no flags
            if (holder.canSwipe()) swipeFlags else 0 // If the holder can swipe, use the swipe flags; otherwise, use no flags
        )
    }

    /**
     * Called when an item is moved within the RecyclerView.
     *
     * @param recyclerView The RecyclerView where the movement occurred.
     * @param viewHolder   The source view holder.
     * @param target       The target view holder.
     * @return True if the item was moved successfully; false otherwise.
     */
    override fun onMove(recyclerView: RecyclerView, source: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return gestureAdapter.onItemMove(source.adapterPosition, target.adapterPosition) // Move the item in the adapter
    }

    /**
     * Called when an item is swiped in the RecyclerView.
     *
     * @param viewHolder The view holder that was swiped.
     * @param direction  The direction of the swipe.
     */
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        gestureAdapter.onItemDismissed(viewHolder.adapterPosition, direction) // Dismiss the swiped item in the adapter
    }

    /**
     * Called when an item is selected for a gesture.
     *
     * @param viewHolder The selected view holder.
     * @param actionState The state of the action.
     */
    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE && viewHolder is GestureViewHolder<*>) {
            viewHolder.onItemSelect() // Perform any necessary actions when the item is selected
        }
    }

    /**
     * Called during a gesture to draw the background and foreground views.
     *
     * @param c                 The Canvas to draw on.
     * @param recyclerView      The RecyclerView containing the view holder.
     * @param viewHolder        The view holder to draw.
     * @param dX                The horizontal displacement of the view.
     * @param dY                The vertical displacement of the view.
     * @param actionState       The state of the action.
     * @param isCurrentlyActive True if the gesture is currently active; false otherwise.
     */
    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        when (actionState) {
            ItemTouchHelper.ACTION_STATE_SWIPE -> {
                // Handle swipe gestures
                val direction = when {
                    dX.compareTo(0f) == 0 -> if (dY < 0) ItemTouchHelper.UP else ItemTouchHelper.DOWN
                    dY.compareTo(0f) == 0 -> if (dX < 0) ItemTouchHelper.LEFT else ItemTouchHelper.RIGHT
                    else -> -1
                }

                val gestureViewHolder = (viewHolder as GestureViewHolder<*>)
                hideBackgroundViews(gestureViewHolder) // Hide any previously visible background views

                if (direction != -1) {
                    val backgroundView = gestureViewHolder.getBackgroundView(direction)
                    backgroundView?.let {
                        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && backgroundView.visibility == View.GONE) {
                            backgroundView.visibility = View.VISIBLE // Show the background view if it's not already visible
                        }
                    }
                }

                val foregroundView = gestureViewHolder.foregroundView
                getDefaultUIUtil().on
