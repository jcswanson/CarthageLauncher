package com.thesurix.gesturerecycler

import android.graphics.Canvas
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.*
import com.thesurix.gesturerecycler.LayoutFlags.*

/**
 * Touch helper callback that handles different RecycleView gestures.
 * Constructs callback object based on passed adapter.
 * @param adapter adapter
 * @author thesurix
 */
@Suppress("UNUSED_PARAMETER")
private val DIRECTIONS = listOf(ItemTouchHelper.LEFT, ItemTouchHelper.RIGHT, ItemTouchHelper.UP, ItemTouchHelper.DOWN)
class GestureTouchHelperCallback @JvmOverloads constructor(
    private val gestureAdapter: GestureAdapter<*, *>,
    swipeEnabled: Boolean = false,
    manualDragEnabled: Boolean = false,
    longPressDragEnabled: Boolean = false,
    dragFlags: Int = ItemTouchHelper.UP or ItemTouchHelper.DOWN,
    swipeFlags: Int = ItemTouchHelper.RIGHT
) : ItemTouchHelper.Callback() {

    /** Flag that enables or disables swipe gesture  */
    var swipeEnabled: Boolean = swipeEnabled
        set(value) {
            field = value
            gestureAdapter.allowSwipe(value)
        }

    /** Flag that enables or disables manual drag gesture  */
    var manualDragEnabled: Boolean = manualDragEnabled
        set(value) {
            field = value
            gestureAdapter.allowManualDrag(value)
        }

    /** Flag that enables long press drag gesture  */
    var longPressDragEnabled: Boolean = longPressDragEnabled

    /** Flags for drag gesture  */
    var dragFlags: Int = dragFlags

    /** Flags for swipe gesture  */
    var swipeFlags: Int = swipeFlags

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val holder = viewHolder as GestureViewHolder<*>
        return makeMovementFlags(
            if (holder.canDrag()) dragFlags else 0,
            if (holder.canSwipe()) swipeFlags else 0
        )
    }

    override fun onMove(recyclerView: RecyclerView, source: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return gestureAdapter.onItemMove(source.adapterPosition, target.adapterPosition)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        gestureAdapter.onItemDismissed(viewHolder.adapterPosition, direction)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE && viewHolder is GestureViewHolder<*>) {
            viewHolder.onItemSelect()
        }
    }

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
                val direction = when {
                    dX.compareTo(0f) == 0 -> if (dY < 0) ItemTouchHelper.UP else ItemTouchHelper.DOWN
                    dY.compareTo(0f) == 0 -> if (dX < 0) ItemTouchHelper.LEFT else ItemTouchHelper.RIGHT
                    else -> -1
                }

                val gestureViewHolder = (viewHolder as GestureViewHolder<*>)
                hideBackgroundViews(gestureViewHolder)

                if (direction != -1) {
                    val backgroundView = gestureViewHolder.getBackgroundView(direction)
                    backgroundView?.let {
                        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && backgroundView.visibility == View.GONE) {
                            backgroundView.visibility = View.VISIBLE
                        }
                    }
                }

                val foregroundView = gestureViewHolder.foregroundView
                getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive)
            }
            else -> super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        gestureAdapter.onItemMoved()
        if (viewHolder is Gest
