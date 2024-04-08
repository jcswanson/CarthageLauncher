package com.codesteem.mylauncher.gesture

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * Constants
 */
private const val INVALID_FLAG = -1

/**
 * GestureManager class is responsible for managing gestures for RecyclerView widget.
 * It initializes a GestureTouchHelperCallback object and attaches it to the RecyclerView.
 * The adapter associated with the RecyclerView must extend GestureAdapter.
 *
 * @author thesurix
 */
class GestureManager private constructor(builder: Builder) {

    /**
     * GestureTouchHelperCallback object that handles gesture callbacks.
     */
    private val touchHelperCallback: GestureTouchHelperCallback

    /**
     * Initializes a new instance of GestureManager with the given Builder object.
     *
     * @param builder Builder object containing RecyclerView instance and gesture flags.
     */
    init {
        val adapter = builder.recyclerView.adapter as GestureAdapter<Any, *>
        touchHelperCallback = GestureTouchHelperCallback(adapter).apply {
            swipeEnabled = builder.isSwipeEnabled
            longPressDragEnabled = builder.isDragEnabled
            manualDragEnabled = builder.isManualDragEnabled
        }

        // Attaches the ItemTouchHelper to the RecyclerView.
        val touchHelper = ItemTouchHelper(touchHelperCallback)
        touchHelper.attachToRecyclerView(builder.recyclerView)

        // Sets the GestureListener for the adapter.
        adapter.setGestureListener(GestureListener(touchHelper))

        // Sets the swipe and drag flags for the RecyclerView.
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

        // Sets the header and footer flags for the adapter.
        adapter.setHeaderEnabled(builder.isHeaderEnabled)
        adapter.setFooterEnabled(builder.isFooterEnabled)
    }

    /**
     * Returns true if swipe is enabled, false if swipe is disabled.
     *
     * @return swipe state
     */
    /**
     * Sets swipe gesture enabled or disabled.
     *
     * @param enabled true to enable, false to disable
     */
    var isSwipeEnabled: Boolean
        get() = touchHelperCallback.isItemViewSwipeEnabled
        set(enabled) {
            touchHelperCallback.swipeEnabled = enabled
        }

    /**
     * Returns true if long press drag is enabled, false if long press drag is disabled.
     *
     * @return long press drag state
     */
    /**
     * Sets long press drag gesture enabled or disabled.
     *
     * @param enabled true to enable, false to disable
     */
    var isLongPressDragEnabled: Boolean
        get() = touchHelperCallback.isLongPressDragEnabled
        set(enabled) {
            touchHelperCallback.longPressDragEnabled = enabled
        }

    /**
     * Returns true if manual drag is enabled, false if manual drag is disabled.
     *
     * @return manual drag state
     */
    /**
     * Sets manual drag gesture enabled or disabled.
     *
     * @param enabled true to enable, false to disable
     */
    var isManualDragEnabled: Boolean
        get() = touchHelperCallback.manualDragEnabled
        set(enabled) {
            touchHelperCallback.manualDragEnabled = enabled
        }

    /**
     * Builder class for GestureManager.
     * Constructs a GestureManager for the given RecyclerView.
     *
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
         * Flag for swipe gesture enabled or disabled.
         */
        internal var isSwipeEnabled = false
            private set

        /**
         * Flag for long press drag gesture enabled or disabled.
         */
        internal var isDragEnabled = false
            private set

        /**
         * Flag for manual drag gesture enabled or disabled.
         */
        internal var isManualDragEnabled = false
            private set

        /**
         * Flag for header item enabled or disabled.
         */
        internal var isHeaderEnabled = false
            private set

        /**
         * Flag for footer item enabled or disabled.
         */
        internal var isFooterEnabled = false
            private set

        /**
         * Sets swipe gesture enabled or disabled.
         * Swipe is disabled by default.
         *
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
         *
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
         *
         * @param enabled true to enable, false to disable
         * @return returns builder instance
         */
        fun setManualDragEnabled(enabled: Boolean): Builder {
            isManualDragEnabled = enabled
            return this
        }

        /**
         * Sets
