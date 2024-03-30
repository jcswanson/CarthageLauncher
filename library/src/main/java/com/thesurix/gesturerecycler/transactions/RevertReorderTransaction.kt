/**
 * A [Transaction] implementation that reverts a previous reorder transaction.
 * This class is responsible for undoing the effects of a [RevertOrderTransaction]
 * by restoring the original order of the items in a list.
 *
 * @property from The original index of the item that was moved.
 * @property to The index to which the item was moved.
 * @property headerEnabled A flag indicating whether the list contains headers.
 * If true, the indices used for notifications will be adjusted to account for the header.
 *
 * @author thesurix
 */
class RevertReorderTransaction<T>(private val from: Int,
                                  private val to: Int,
                                  private val headerEnabled: Boolean) : Transaction<T> {

    /**
     * Performs the revert reorder transaction, but does nothing in this implementation.
     * This method is provided for compatibility with the [Transaction] interface.
     *
     * @param transactional The [Transactional] object that will be used to perform the transaction.
     * @return false, as this method does not modify the list.
     */
    override fun perform(transactional: Transactional<T>) = false

    /**
     * Reverts the previous reorder transaction, restoring the original order of the items in the list.
     * This method removes the item at the current index, and inserts it back at its original position.
     *
     * @param transactional The [Transactional] object that will be used to perform the transaction.
     * @return true if the transaction was successful, false otherwise.
     */
    override fun revert(transactional: Transactional<T>): Boolean {
        return with(transactional.data) {
            // Remove the item at the current index
            val item = removeAt(to)
            // If the item was not null, insert it back at its original position
            item?.let {
                transactional.notifyRemoved(to + if (headerEnabled) 1 else 0)
                add(from, it
