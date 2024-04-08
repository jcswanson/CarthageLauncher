/**
 * A functional interface that defines a set of methods for performing transactional updates
 * on a list of data items. This interface is intended to be implemented by classes that
 * need to perform fine-grained updates on a list of items, such as adding, removing, or
 * modifying individual items.
 *
 * @author thesurix
 */
interface Transactional<T> {

    /**
     * The list of data items that this transactional object is responsible for managing.
     * This list should be mutable, as it may be modified by the methods in this interface.
     */
    val data: MutableList<T>

    /**
     * Notifies any registered listeners that the item at the specified position has changed.
     * This method should be called whenever an item in the list is modified in some way.
     *
     * @param position The index of the item that has changed.
     */
    fun notifyChanged(position: Int)

    /**
     * Notifies any registered listeners that a new item has been inserted into the list at
     * the specified position. This method should be called whenever a new item is added to
     * the list.
     *
     * @param position The index at which the new item has been inserted.
     */
    fun notifyInserted(position: Int)

    /**
     * Notifies any registered listeners that the item at the specified position has been
     * removed from the list. This method should be called whenever an item is removed from
     * the list.
     *
     * @param position The index of the item that has been removed.
     */
    fun notifyRemoved(position: Int)

    /**
     * Notifies any registered listeners that the item at the specified position has been
     * moved to a new position in the list. This method should be called whenever an item
     * is moved within the list.
     *
     * @param fromPosition The original index of the item that has been moved.
     * @param toPosition The new index of the item that has been moved.
    
