// Constants
private const val INVALID_FLAG = -1

// GestureManager class
class GestureManager(private val builder: Builder) {

    // Initialize touchHelperCallback with the adapter and set gesture flags
    private val touchHelperCallback: GestureTouchHelperCallback

    init {
        val adapter = builder.recyclerView.adapter as GestureAdapter<Any, *>
        touchHelperCallback = GestureTouchHelperCallback(adapter).apply {
            swipeEnabled = builder.isSwipeEnabled
            longPressDragEnabled = builder.isDragEnabled
            manualDragEnabled = builder.isManualDragEnabled
        }

        // Attach touchHelper to the RecyclerView and set the gesture listener
        val touchHelper = ItemTouchHelper(touchHelperCallback)
        touchHelper.attachToRecyclerView(builder.recyclerView)
        adapter.setGestureListener(GestureListener(touchHelper))

        // Set swipe and drag flags based on the builder flags or predefined flags for the layout manager
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

        // Set header and footer flags for the adapter
        adapter.setHeaderEnabled(builder.isHeaderEnabled)
        adapter.setFooterEnabled(builder.isFooterEnabled)
    }

    // Getter and setter methods for gesture flags
    var isSwipeEnabled: Boolean
        get() = touchHelperCallback.isItemViewSwipeEnabled
        set(enabled) {
            touchHelperCallback.swipeEnabled = enabled
        }

    var isLongPressDragEnabled: Boolean
        get() = touchHelperCallback.isLongPressDragEnabled
        set(enabled) {
            touchHelperCallback.longPressDragEnabled = enabled
        }

    var isManualDragEnabled: Boolean
        get() = touchHelperCallback.manualDragEnabled
        set(enabled) {
            touchHelperCallback.manualDragEnabled = enabled
        }
}

// Builder class for GestureManager
class Builder(val recyclerView: RecyclerView) {

    // Builder properties for gesture flags and header/footer flags
    internal var swipeFlags = INVALID_FLAG
        private set
    internal var dragFlags = INVALID_FLAG
        private set
    internal var isSwipeEnabled = false
        private set
    internal var isDragEnabled = false
        private set
    internal var isManualDragEnabled = false
        private set
    internal var isHeaderEnabled = false
        private set
    internal var isFooterEnabled = false
        private set

    // Builder methods for setting gesture flags and header/footer flags
    fun setSwipeEnabled(enabled: Boolean): Builder {
        isSwipeEnabled = enabled
        return this
    }

    fun setLongPressDragEnabled(enabled: Boolean): Builder {
        isDragEnabled = enabled
        return this
    }

    fun setManualDragEnabled(enabled: Boolean): Builder {
        isManualDragEnabled = enabled
        return this
    }

    @Deprecated("Use setSwipeFlags() and setDragFlags() methods.")
    fun setGestureFlags(swipeFlags: Int, dragFlags: Int): Builder {
        this.swipeFlags = swipeFlags
        this.dragFlags = dragFlags
        return this
    }

    fun setSwipeFlags(flags: Int): Builder {
        swipeFlags = flags
        return this
    }

    fun setDragFlags(flags: Int): Builder {
        dragFlags = flags
        return this
    }

    fun setHeaderEnabled(enabled: Boolean): Builder {
        isHeaderEnabled = enabled
        return this
    }

    fun setFooterEnabled(enabled: Boolean): Builder {
        isFooterEnabled = enabled
        return this
    }

    // Method for building and validating the GestureManager instance
    fun build(): GestureManager {
        validateBuilder()
        return GestureManager(this)
    }

    // Method for validating the builder properties
    private fun validateBuilder() {
        val hasAdapter = recyclerView.adapter is GestureAdapter<*, *>
        if (!hasAdapter) {
            throw IllegalArgumentException("RecyclerView does not have adapter that extends " + GestureAdapter::class.java.name)
        }

        if (swipeFlags == INVALID_FLAG || dragFlags == INVALID_FLAG) {
            if (recyclerView.layoutManager == null) {
                throw IllegalArgumentException("No layout manager for RecyclerView. Provide custom flags or attach layout manager to RecyclerView.")
            }
        }
    }
}
