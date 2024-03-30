package com.thesurix.gesturerecycler

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * Constants
 */
private const val INVALID_FLAG = -1

/**
 * Class that is responsible for gesture management for [RecyclerView] widget.
 * Users can enable or disable swipe and drag gestures, and set custom flags for each gesture.
 * The class also supports header and footer items.
 * @author thesurix
 */
class GestureManager private constructor(
    private val touchHelperCallback: GestureTouchHelperCallback,
    private val recyclerView: RecyclerView,
    private val adapter: GestureAdapter<*, *>
) {

    /**
     * Initializes a new instance of the GestureManager class with the provided builder.
     * The builder is used to configure the GestureManager instance with the desired settings.
     * @param builder Builder instance containing the configuration for the GestureManager
     */
    init {
        recyclerView.adapter = adapter
        ItemTouchHelper(touchHelperCallback).attachToRecyclerView(recyclerView)
        adapter.setGestureListener(GestureListener(touchHelperCallback))

        setSwipeAndDragFlags()
        setHeaderAndFooterFlags()
    }

    /**
     * Sets swipe and drag flags based on the builder configuration.
     */
    private fun setSwipeAndDragFlags() {
        with(touchHelperCallback) {
            swipeEnabled = builder.isSwipeEnabled
            longPressDragEnabled = builder.isDragEnabled
            manualDragEnabled = builder.isManualDragEnabled

            if (builder.swipeFlags == INVALID_FLAG) {
                setSwipeFlagsForLayout(recyclerView.layoutManager!!)
            } else {
                swipeFlags = builder.swipeFlags
            }

            if (builder.dragFlags == INVALID_FLAG) {
                setDragFlagsForLayout(recyclerView.layoutManager!!)
            } else {
                dragFlags = builder.dragFlags
            }
        }
    }

    /**
     * Sets header and footer enabled flags for the adapter.
     */
    private fun setHeaderAndFooterFlags() {
        adapter.setHeaderEnabled(builder.isHeaderEnabled)
        adapter.setFooterEnabled(builder.isFooterEnabled)
    }

    /**
     * Class that builds [GestureManager] instance.
     * Constructs [GestureManager] for the given RecyclerView.
     * @param recyclerView RecyclerView instance
     */
    class Builder(private val recyclerView: RecyclerView) {

        /**
         * Flags for swipe gesture.
         * See [ItemTouchHelper] flags.
         */
        internal var swipeFlags = INVALID_FLAG
            private set

        /**
         * Flags for drag gesture.
         * See [ItemTouchHelper] flags.
         */
        internal var dragFlags = INVALID_FLAG
            private set

        /**
         * Indicates whether swipe gesture is enabled or disabled.
         * Swipe is disabled by default.
         */
        internal var isSwipeEnabled = false
            private set

        /**
         * Indicates whether long press drag gesture is enabled or disabled.
         * Long press drag is disabled by default.
         */
        internal var isDragEnabled = false
            private set

        /**
         * Indicates whether manual drag gesture is enabled or disabled.
         * Manual drag is disabled by default.
         */
        internal var isManualDragEnabled = false
            private set

        /**
         * Indicates whether header item is enabled or disabled.
         * If enabled, [RecyclerView.Adapter.onCreateViewHolder] will get [TYPE_HEADER_ITEM] as a viewType argument.
         * Header is disabled by default.
         */
        internal var isHeaderEnabled = false
            private set

        /**
         * Indicates whether footer item is enabled or disabled.
         * If enabled, [RecyclerView.Adapter.onCreateViewHolder] will get [TYPE_FOOTER_ITEM] as a viewType argument.
         * Footer is disabled by default.
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
        fun setDragEnabled(enabled: Boolean): Builder {
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
         * Sets header item enabled or disabled.
         * Header is disabled by default.
         * @param enabled true to enable, false to disable
         * @return returns builder instance
         */
        fun setHeaderEnabled(enabled: Boolean): Builder {
            isHeaderEnabled = enabled
            return this
        }

        /**
         * Sets footer item enabled or disabled.
         * Footer is disabled by default.
         * @param enabled true to enable, false to disable
         * @return returns builder instance
         */
        fun setFooterEnabled(enabled: Boolean): Builder {
            isFooterEnabled = enabled
            return this
        }

        /**
         * Sets swipe and drag flags.
         * @param swipeFlags Flags for swipe gesture.
         * @param dragFlags Flags for drag gesture.
         * @return returns builder instance
         */
        fun setFlags(swipeFlags: Int, drag
