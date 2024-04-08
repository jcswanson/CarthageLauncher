/**
 * A [Transaction] implementation that reverts a previous reorder transaction.
 * This class is responsible for undoing the effects of a [RevertOrderTransaction]
 * by restoring the original order of the items in a list.
 *
 * @property from The original index of the item that was moved.
 * @property to The index to which the item was moved.
 * @property headerEnabled A flag indicating whether the list contains headers or not.
 * If true, the indices of inserted and removed items will be adjusted by 1 to account
 * for the presence of a header.
 *
 * @author thesurix
 */
class RevertReorderTransaction<T>(private val from: Int,
                                  private val to: Int,
                                  private val headerEnabled: Boolean) : Transaction<T> {

    /**
     * Performs the revert reorder transaction, but does nothing in this implementation.
     * This method is provided for consistency with the [Transaction] interface.
     *
     * @param transactional The [Transactional] object that contains the data to be modified.
     * @return false, as the transaction is not performed in this implementation.
     */
    override fun perform(transactional: Transactional<T>): Boolean = false

    /**
     * Reverts the previous reorder transaction by restoring the original order of the items
     * in the list. This method removes the item at the current index and inserts it back
     * at its original index.
     *
     * @param transactional The [Transactional] object that contains the data to be modified.
     * @return true if the revert operation was successful, false otherwise.
     */
    override fun revert(transactional: Transactional<T>): Boolean {
        return with(transactional.data) {
            // Remove the item at the current index and store it in a variable.
            val item = removeAt(to)
            // If the item exists, proceed with the revert operation.
            item?.let {
                // Notify the observer that an
