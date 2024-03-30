/**
 * Interface for a transaction that can be performed and reverted on a [Transactional] object.
 *
 * A transaction represents a single unit of work that can be executed and then rolled back if necessary.
 * This interface defines two methods, `perform` and `revert`, that allow a transaction to be executed
 * and then undone, respectively.
 *
 * @param T The type of object that this transaction can be performed on.
 *
 * @author thesurix
 */
interface Transaction<T> {
    /**
     * Performs this transaction on the given [transactional] object.
     *
     * This method should execute the transaction and make any necessary changes to the [transactional]
     * object. If the transaction is successful, this method should return `true`. If the transaction
     * fails for any reason, this method should return `false`.
    
