/**
 * Observer class for managing visibility of the adapter's empty view. This class is responsible for
 * showing or hiding the empty view based on the number of items in the adapter. It also notifies
 * the [EmptyViewVisibilityListener] when the visibility state changes.
 */
internal class EmptyViewDataObserver : RecyclerView.AdapterDataObserver() {

    /**
     * The RecyclerView that this observer is monitoring. This is used to determine whether the
     * RecyclerView is empty or not.
     */
    var recyclerView: RecyclerView? = null
        set(value) {
            field = value
            updateEmptyViewState()
        }

    /**
     * The empty view that is displayed when the RecyclerView is empty. This is used to show or hide
     * the empty view based on the number of items in the adapter.
     */
    var emptyView: View? = null
        set(value) {
            field = value
            updateEmptyViewState()
        }

    /**
     * The listener that is notified when the visibility state of the empty view changes.
     */
    var emptyViewVisibilityListener: EmptyViewVisibilityListener? = null

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
     * or not, and updates the visibility of the empty view and RecyclerView accordingly. It
