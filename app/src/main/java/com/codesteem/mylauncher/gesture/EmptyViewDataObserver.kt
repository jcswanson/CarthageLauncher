/**
 * Observer class for managing visibility of the adapter's empty view. This class is responsible for
 * showing or hiding the empty view based on the number of items in the adapter. It also notifies
 * the [EmptyViewVisibilityListener] when the visibility state changes.
 */
internal class EmptyViewDataObserver(
    private val recyclerView: RecyclerView,
    private val emptyView: View,
    private val emptyViewVisibilityListener: EmptyViewVisibilityListener
) : RecyclerView.AdapterDataObserver() {

    /**
     * Called when the data in the adapter changes. This method checks whether the adapter is empty
     * or not, and updates the visibility of the empty view and RecyclerView accordingly. It also
     * notifies the [EmptyViewVisibilityListener] when the visibility state changes.
     */
    override fun onChanged() {
        updateEmptyViewState()
    }

    /**
     * Called when items are added to the adapter. This method checks whether the adapter is empty
     * or not, and updates the visibility of the empty view and RecyclerView accordingly. It also
     * notifies the [EmptyViewVisibilityListener] when the visibility state changes.
     */
    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        updateEmptyViewState()
    }

    /**
     * Called when items are removed from the adapter. This method checks whether the adapter is empty
     * or not, and updates the visibility of the empty view and RecyclerView accordingly. It also
     * notifies the [EmptyViewVisibilityListener] when the visibility state changes.
     */
    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        updateEmptyViewState()
    }

    /**
     * Updates the visibility state of the empty view and RecyclerView.
     */
    private fun updateEmptyViewState() {
        val isEmpty = recyclerView.adapter?.itemCount ?: 0 == 0
        emptyView.visibility = if (isEmpty) View.VISIBLE else View.GONE
        recyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE
        emptyViewVisibilityListener.onEmptyViewVisibilityChanged(isEmpty)
    }
}

/**
 * Listener interface for empty view visibility changes.
 */
interface EmptyViewVisibilityListener {

    /**
     * Called when the visibility state of the empty view changes.
     */
    fun onEmptyViewVisibilityChanged(isEmpty: Boolean)
}
