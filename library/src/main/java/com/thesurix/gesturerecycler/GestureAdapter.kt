/**
 * Base adapter for gesture recognition. This adapter should be extended to provide a custom implementation.
 * It handles drag and swipe gestures for items in the RecyclerView.
 *
 * @author thesurix
 */
const val TYPE_HEADER_ITEM = 456789
const val TYPE_FOOTER_ITEM = TYPE_HEADER_ITEM + 1

interface OnDataChangeListener<T> {
    fun onItemRemoved(position: Int)
    fun onItemReordered(fromPosition: Int, toPosition: Int)
}

interface OnGestureListener<T> {
    fun onStartDrag(viewHolder: RecyclerView.ViewHolder)
}

class GestureAdapter<T>(
    private val data: MutableList<T>,
    private val gestureListener: OnGestureListener<T>? = null,
    private val dataChangeListener: OnDataChangeListener<T>? = null,
    private val manualDragAllowed: Boolean = false,
    private val headerEnabled: Boolean = false,
    private val footerEnabled: Boolean = false,
    private val undoSize: Int = 1
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Transactional<T> {

    private var swappedItem: T? = null
    private var startDragPos = RecyclerView.NO_POSITION
    private var stopDragPos = RecyclerView.NO_POSITION
    private val transactions = FixedSizeArrayDequeue<Transaction<T>>(undoSize)

    private val emptyViewDataObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            super.onItemRangeRemoved(positionStart, itemCount)
            if (itemCount > 0 && data.isEmpty()) notifyItemRangeInserted(0, 1)
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount)
            if (itemCount > 0 && data.isEmpty()) notifyItemRangeRemoved(0, 1)
        }
    }

    init {
        registerAdapterDataObserver(emptyViewDataObserver)
    }

    override fun getItemViewType(position: Int): Int {
        if (headerEnabled && position == 0) return TYPE_HEADER_ITEM
        if (footerEnabled && position == itemCount - 1) return TYPE_FOOTER_ITEM
        return 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty() && holder is GestureViewHolder<T>) {
            holder.bind(data[position])
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is GestureViewHolder<T> && manualDragAllowed) {
            holder.itemView.setOnTouchListener(holder.createItemTouchHelperGestureListener(gestureListener))
        }
        (holder as? GestureViewHolder<T>)?.bind(data[position])
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        (holder as? GestureViewHolder<T>)?.onViewRecycled()
    }

    override fun getItemCount(): Int = data.size + if (headerEnabled) 1 else 0 + if (footerEnabled) 1 else 0

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        if (recyclerView.layoutManager is LinearLayoutManager) {
            (recyclerView.layoutManager as LinearLayoutManager).onItemsMoveFinished(object : LinearLayoutManager.OnItemsMoveFinishedCallback {
                override fun onItemsMoveFinished() {
                    dataChangeListener?.onItemReordered(startDragPos, stopDragPos)
                }
            })
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        transactions.clear()
        registerAdapterDataObserver(emptyViewDataObserver)
    }

    override fun notifyChanged(position: Int) {
        notifyItemChanged(position)
    }

    override fun notifyInserted(position: Int) {
        notifyItemInserted(position)
    }

    override fun notifyRemoved(position: Int) {
        notifyItemRemoved(position)
    }

    override fun notifyMoved(fromPosition: Int, toPosition: Int) {
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun setItem(position: Int, item: T) {
        data[position] = item
        notifyItemChanged(position)
    }

    override fun add(item: T): Boolean {
        val position = data.size
        data.add(item)
        notifyItemInserted(position)
        return true
    }

    override fun remove(position: Int): Boolean {
        if (position !in 0 until data.size) return false
        swappedItem = data.removeAt(position)
        notifyItemRemoved(position)
        dataChangeListener?.onItemRemoved(position)
        return true
    }

    override fun insert(item: T, position: Int) {
        data.add(position, item)
        notifyItemInserted(position)
    }

    override fun move(fromPosition: Int, toPosition: Int): Boolean {
        if (fromPosition !in 0 until data.size || toPosition !in 0 until data.size) return false
        val item = data.removeAt(fromPosition)
        data.add(toPosition, item)
        notifyItemMoved(fromPosition, toPosition)
        dataChangeListener?.onItemReordered(fromPosition, toPosition)
        return true
    }

    override fun swap(firstPosition: Int, secondPosition: Int): Boolean {
        if (firstPosition !in 0 until data.size || secondPosition !in 0 until data.size) return false
        val firstItem = data
