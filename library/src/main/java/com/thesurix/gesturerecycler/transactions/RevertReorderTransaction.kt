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
class RevertReorderTransaction<T>(
    private val from: Int,
    private val to: Int,
    private val headerEnabled: Boolean
) : Transaction<T> {

    override fun perform(transactional: Transactional<T>): Boolean {
        // Perform the reorder transaction
        transactional.data.add(to, transactional.data.removeAt(from))
        if (headerEnabled) {
            transactional.notifyRemoved(from + 1)
            transactional.notifyAdded(to + 1)
        } else {
            transactional.notifyRemoved(from)
            transactional.notifyAdded(to)
        }
        return true
    }

    override fun revert(transactional: Transactional<T>): Boolean {
        return with(transactional.data) {
            // Revert the reorder transaction
            add(from, removeAt(to))
            if (headerEnabled) {
                transactional.notifyAdded(from + 1)
                transactional.notifyRemoved(to + 1)
            } else {
                transactional.notifyAdded(from)
                transactional.notifyRemoved(to)
            }
            return true
        }
    }
}
