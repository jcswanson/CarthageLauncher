/**
 * Observer class for managing visibility of the adapter's empty view. This class is responsible for
 * showing or hiding the empty view based on the number of items in the adapter. It also notifies
 * the [EmptyViewVisibilityListener] when the visibility of the empty view changes.
 */
internal class EmptyViewDataObserver : RecyclerView.AdapterDataObserver() {

    /**
     * The RecyclerView that this observer is monitoring. This property is used to determine whether
     * or not the RecyclerView is empty.
     */
    var recyclerView: RecyclerView? = null
        set(value) {
            field = value
            updateEmptyViewState()
        }

    /**
     * The empty view that will be shown or hidden based on the number of items in the adapter.
     */
    var emptyView: View? = null
        set(value) {
            field = value
            updateEmptyViewState()
        }

    /**
     * The listener that will be notified when the visibility of the empty view changes.
     */
    var emptyViewVisibilityListener: EmptyViewVisibilityListener? = null

    /**
     * Called when the data in the adapter changes. This method checks whether the adapter is empty
     * and updates the visibility of the empty view and RecyclerView accordingly. It also notifies
     * the [EmptyViewVisibilityListener] when the visibility of the empty view changes.
     */
    override fun onChanged() {
        updateEmptyViewState()
    }

    /**
     * Called when items are added to the adapter. This method checks whether the adapter is empty
     * and updates the visibility of the empty view and RecyclerView accordingly. It also notifies
     * the [EmptyViewVisibilityListener] when the visibility of the empty view changes.
     */
    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        updateEmptyViewState()
    }

    /**
     * Called when items are removed from the adapter. This method checks whether the adapter is empty
     * and updates the visibility of the empty view and RecyclerView accordingly. It also notifies
     * the [EmptyViewVisibilityListener] when the visibility of the
