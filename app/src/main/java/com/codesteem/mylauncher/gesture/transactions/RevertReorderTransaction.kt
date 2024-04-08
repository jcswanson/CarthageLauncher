/**
 * A [Transaction] implementation that reverts a previous reorder transaction.
 * This class is responsible for undoing the effects of a [RevertReorderTransaction.perform] call.
 *
 * @property from The original index of the item being moved.
 * @property to The destination index of the item being moved.
 * @property headerEnabled A flag indicating whether the list contains headers.
 *                          If true, the indices of inserted and removed items will be adjusted
 *                          to account for the presence of headers.
 */
class RevertReorderTransaction<T>(private val from: Int,
                                  private val to: Int,
                                  private val headerEnabled: Boolean) : Transaction<T> {

    /**
     * Performs the revert operation by doing nothing. This method is a no-op because reverting a
     * transaction should only affect the data in the [Transactional] object, not the object itself.
     *
     * @param transactional The [Transactional] object containing the data to be modified.
     * @return false, indicating that the transaction was not performed.
     */
    override fun perform(transactional: Transactional<T>): Boolean = false

    /**
     * Reverts the effects of a previous reorder transaction. This method removes the item at the
     * destination index, and inserts it back at the original index.
     *
     * If [headerEnabled] is true, the indices of inserted and removed items will be adjusted
     * to account for the presence of headers.
     *
     * @param transactional The [Transactional] object containing the data to be modified.
     * @return true if the revert operation was successful, false otherwise.
     */
    override fun revert(transactional: Transactional<T>): Boolean {
        return with(transactional.data) {
            // Remove the item at the destination index
            val item = removeAt(to)

            // If the item was successfully removed, insert it back at the original index
            item?.let {
                transactional
