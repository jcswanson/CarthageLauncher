/**
 * This class, `RecyclerItemTouchListener`, is responsible for handling touch events in a [RecyclerView].
 * It constructs a touch listener for the RecyclerView.
 *
 * @param listener The listener for item's click events.
 * @author thesurix
 */
class RecyclerItemTouchListener<T>(listener: ItemClickListener<T>) : RecyclerView.SimpleOnItemTouchListener() {

    // A GestureDetector is used to recognize various gestures performed on the RecyclerView.
    private var gestureDetector: GestureDetector? = null

    /**
     * The listener interface for handling tap, long press, and double tap events on items.
     */
    interface ItemClickListener<T> {

        /**
         * Called when a tap occurs on a specified item.
         *
         * @param item       The pressed item.
         * @param position   The position of the item in the RecyclerView.
         * @return true if the event is consumed, else false.
         */
        fun onItemClick(item: T, position: Int): Boolean

        /**
         * Called when a long press occurs on a specified item.
         *
         * @param item       The pressed item.
         * @param position   The position of the item in the RecyclerView.
         */
        fun onItemLongPress(item: T, position: Int)

        /**
         * Called when a double tap occurs on a specified item.
         *
         * @param item       The tapped item.
         * @param position   The position of the item in the RecyclerView.
         * @return true if the event is consumed, else false.
         */
        fun onDoubleTap(item: T, position: Int): Boolean
    }

    // A private class for handling gesture events and notifying the listener.
    private class GestureClickListener<T>(private val listener: RecyclerItemTouchListener.ItemClickListener<T>)
        : GestureDetector.SimpleOnGestureListener() {

        // The currently touched item and its position in the RecyclerView.
        private var item: T? = null
        private var viewPosition = 0

        // Handles single tap events and notifies the listener.
        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            return item?.let { listener.onItemClick(it, viewPosition) } ?: false
        }

        // Handles long press events and notifies the listener.
        override fun onLongPress(e: MotionEvent) {
            item?.let { listener.onItemLongPress(it, viewPosition) }
        }

        // Handles double tap events and notifies the listener.
        override fun onDoubleTap(e: MotionEvent): Boolean {
            return item?.let { listener.onDoubleTap(it, viewPosition) } ?: false
        }

        // Sets the touched item and its position in the RecyclerView.
        internal fun setTouchedItem(item: T, viewPosition: Int) {
            this.item = item
            this.viewPosition = viewPosition
        }
    }
}
