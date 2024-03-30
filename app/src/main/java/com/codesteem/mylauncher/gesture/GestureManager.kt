// Constants
private const val INVALID_FLAG = -1

// GestureManager class
class GestureManager(private val recyclerView: RecyclerView) {

    private val touchHelperCallback: GestureTouchHelperCallback
    private val gestureListener: GestureListener

    init {
        val adapter = recyclerView.adapter as GestureAdapter<Any, *>
        touchHelperCallback = GestureTouchHelperCallback(adapter)
        gestureListener = GestureListener(touchHelperCallback)

        setGestureFlags()
        setHeaderAndFooterFlags()

        val touchHelper = ItemTouchHelper(touchHelperCallback)
        touchHelper.attachToRecyclerView(recyclerView)
        adapter.setGestureListener(gestureListener)
    }

    fun enableSwipe(enabled: Boolean) {
        touchHelperCallback.swipeEnabled = enabled
        gestureListener.isSwipeEnabled = enabled
    }

    fun enableLongPressDrag(enabled: Boolean) {
        touchHelperCallback.longPressDragEnabled = enabled
        gestureListener.isLongPressDragEnabled = enabled
    }

    fun enableManualDrag(enabled: Boolean) {
        touchHelperCallback.manualDragEnabled = enabled
        gestureListener.isManualDragEnabled = enabled
    }

    private fun setGestureFlags() {
        with(touchHelperCallback) {
            swipeFlags = getSwipeFlagsForLayout(recyclerView.layoutManager!!)
            dragFlags = getDragFlagsForLayout(recyclerView.layoutManager!!)
        }

        with(gestureListener) {
            isSwipeEnabled = swipeFlags != 0
            isLongPressDragEnabled = dragFlags != 0
            isManualDragEnabled = touchHelperCallback.manualDragEnabled
        }
    }

    private fun setHeaderAndFooterFlags() {
        (recyclerView.adapter as GestureAdapter<Any, *>).apply {
            setHeaderEnabled(isHeaderEnabled)
            setFooterEnabled(isFooterEnabled)
        }
    }

    companion object {
        internal fun getSwipeFlagsForLayout(layoutManager: RecyclerView.LayoutManager?): Int {
            return when (layoutManager) {
                is LinearLayoutManager -> if (layoutManager.canScrollHorizontally()) 0 else ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                is GridLayoutManager -> if (layoutManager.canScrollHorizontally()) 0 else ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                is StaggeredGridLayoutManager -> if (layoutManager.canScrollHorizontally()) 0 else ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                else -> INVALID_FLAG
            }
        }

        internal fun getDragFlagsForLayout(layoutManager: RecyclerView.LayoutManager?): Int {
            return when (layoutManager) {
                is LinearLayoutManager -> ItemTouchHelper.UP or ItemTouchHelper.DOWN
                is GridLayoutManager -> ItemTouchHelper.UP or ItemTouchHelper.DOWN
                is StaggeredGridLayoutManager -> ItemTouchHelper.UP or ItemTouchHelper.DOWN
                else -> INVALID_FLAG
            }
        }
    }
}

// Builder class for GestureManager
class Builder(val recyclerView: RecyclerView) {

    internal var isSwipeEnabled = false
    internal var isDragEnabled = false
    internal var isManualDragEnabled = false
    internal var isHeaderEnabled = false
    internal var isFooterEnabled = false

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

    fun setGestureFlags(swipeFlags: Int, dragFlags: Int): Builder {
        isSwipeEnabled = swipeFlags != 0
        isDragEnabled = dragFlags != 0
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

    fun build(): GestureManager {
        validateBuilder()
        return GestureManager(recyclerView)
    }

    private fun validateBuilder() {
        if (recyclerView.adapter !is GestureAdapter<*, *>) {
            throw IllegalArgumentException("RecyclerView does not have adapter that extends " + GestureAdapter::class.java.name)
        }
    }
}
