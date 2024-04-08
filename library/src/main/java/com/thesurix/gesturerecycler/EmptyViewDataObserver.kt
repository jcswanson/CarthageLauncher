/**
 * Observer class for managing visibility of the adapter's empty view. This class extends
 * RecyclerView.AdapterDataObserver and overrides necessary methods to detect changes in the
 * RecyclerView's data set. It maintains references to the RecyclerView and empty view, and
 * updates the visibility of both based on whether the data set is empty or not.
 */
internal class EmptyViewDataObserver : RecyclerView.AdapterDataObserver() {

    /**
     * The RecyclerView that this observer is monitoring. This is used to detect changes in the
     * data set and update the visibility of the empty view accordingly.
     */
    var recyclerView: RecyclerView? = null
        set(value) {
            field = value
            // Call updateEmptyViewState() whenever recyclerView is set or changed
            updateEmptyViewState()
        }

    /**
     * The empty view that is displayed when the RecyclerView's data set is empty. This is used
     * in conjunction with recyclerView to manage the visibility of the empty view.
     */
    var emptyView: View? = null
        set(value) {
            field = value
            // Call updateEmptyViewState() whenever emptyView is set or changed
            updateEmptyViewState()
        }

    /**
     * The listener that is notified whenever the visibility of the empty view changes. This
     * allows other parts of the application to respond to changes in the visibility of the
     * empty view.
     */
    var emptyViewVisibilityListener: EmptyViewVisibilityListener? = null

    /**
     * Called when the data set changes. This method updates the visibility of the empty view
     * based on whether the data set is empty or not.
     */
    override fun onChanged() {
        updateEmptyViewState()
    }

    /**
     * Called when items are inserted into the data set. This method updates the visibility of
     * the empty view based on whether the data set is empty or not.
     */
    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        updateEmptyViewState()
    }

    /**
     * Called
