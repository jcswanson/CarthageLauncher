package com.thesurix.gesturerecycler

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * Constant value for an invalid flag.
 */
private const val INVALID_FLAG = -1

/**
 * Class that is responsible for gesture management for [RecyclerView] widget.
 * Users can customize gesture behavior using the [Builder] class.
 * @author thesurix
 */
class GestureManager private constructor(builder: Builder) {

    /**
     * Callback object that handles gesture events for the RecyclerView.
     */
    private val touchHelperCallback: GestureTouchHelperCallback

    /**
     * Initializes a new instance of the GestureManager class with the specified builder.
     */
    init {
        val adapter = builder.recyclerView.adapter as GestureAdapter<Any, *>
        touchHelperCallback = GestureTouchHelperCallback(adapter).apply {
            swipeEnabled = builder.isSwipeEnabled
            longPressDragEnabled = builder.isDragEnabled
            manualDragEnabled = builder.isManualDragEnabled
        }

        // Attaches the touch helper to the RecyclerView.
        val touchHelper = ItemTouchHelper(touchHelperCallback)
        touchHelper.attachToRecyclerView(builder.recyclerView)

        // Sets the gesture listener for the adapter.
        adapter.setGestureListener(GestureListener(touchHelper))

        // Sets the swipe and drag flags based on the builder settings.
        if (builder.swipeFlags == INVALID_FLAG) {
            touchHelperCallback.setSwipeFlagsForLayout(builder.recyclerView.layoutManager!!)
        } else {
            touchHelperCallback.swipeFlags = builder.swipeFlags
        }

        if (builder.dragFlags == INVALID_FLAG) {
            touchHelperCallback.setDragFlagsForLayout(builder.recyclerView.layoutManager!!)
        } else {
            touchHelperCallback.dragFlags = builder.dragFlags
        }

        // Sets the header and footer enabled flags for the adapter.
        adapter.setHeaderEnabled(builder.isHeaderEnabled)
        adapter.setFooterEnabled(builder.isFooterEnabled)
    }

    /**
     * Returns true if swipe is enabled, false if swipe is disabled.
     * @return swipe state
     */
    /**
     * Sets swipe gesture enabled or disabled.
     * @param enabled true to enable, false to disable
     */
    var isSwipeEnabled: Boolean
        get() = touchHelperCallback.isItemViewSwipeEnabled
        set(enabled) {
            touchHelperCallback.swipeEnabled = enabled
        }

    /**
     * Returns true if long press drag is enabled, false if long press drag is disabled.
     * @return long press drag state
     */
    /**
     * Sets long press drag gesture enabled or disabled.
     * @param enabled true to enable, false to disable
     */
    var isLongPressDragEnabled: Boolean
        get() = touchHelperCallback.isLongPressDragEnabled
        set(enabled) {
            touchHelperCallback.longPressDragEnabled = enabled
        }

    /**
     * Returns true if manual drag is enabled, false if manual drag is disabled.
     * @return manual drag state
     */
    /**
     * Sets manual drag gesture enabled or disabled.
     * @param enabled true to enable, false to disable
     */
    var isManualDragEnabled: Boolean
        get() = touchHelperCallback.manualDragEnabled
        set(enabled) {
            touchHelperCallback.manualDragEnabled = enabled
        }

    /**
     * Class that builds [GestureManager] instance.
     * Constructs [GestureManager] for the given RecyclerView.
     * @param recyclerView RecyclerView instance
     */
    class Builder(val recyclerView: RecyclerView) {

        /**
         * Flags for swipe gesture.
         */
        internal var swipeFlags = INVALID_FLAG
            private set

        /**
         * Flags for drag gesture.
         */
        internal var dragFlags = INVALID_FLAG
            private set

        /**
         * True if swipe is enabled, false if swipe is disabled.
         */
        internal var isSwipeEnabled = false
            private set

        /**
         * True if long press drag is enabled, false if long press drag is disabled.
         */
        internal var isDragEnabled = false
            private set

        /**
         * True if manual drag is enabled, false if manual drag is disabled.
         */
        internal var isManualDragEnabled = false
            private set

        /**
         * True if header item is enabled, false if header item is disabled.
         */
        internal var isHeaderEnabled = false
            private set

        /**
         * True if footer item is enabled, false if footer item is disabled.
         */
        internal var isFooterEnabled = false
            private set

        /**
         * Sets swipe gesture enabled or disabled.
         * Swipe is disabled by default.
         * @param enabled true to enable, false to disable
         * @return returns builder instance
         */
        fun setSwipeEnabled(enabled: Boolean): Builder {
            isSwipeEnabled = enabled
            return this
        }

        /**
         * Sets long press drag gesture enabled or disabled.
         * Long press drag is disabled by default.
         * @param enabled true to enable, false to disable
         * @return returns builder instance
         */
        fun setLongPressDragEnabled(enabled: Boolean): Builder {
            isDragEnabled = enabled
            return this
        }

        /**
         * Sets manual drag gesture enabled or disabled.
         * Manual drag is disabled by default.
         * @param enabled true to enable, false to disable
         * @return returns builder instance
         */
        fun setManualDragEnabled(enabled: Boolean): Builder {
            isManualDragEnabled = enabled
            return this
        }

        /**
         * Sets flags for swipe and drag gesture. Do not set this flags if you want predefined flags for RecyclerView layout manager.
         * See [ItemTouchHelper] flags.

