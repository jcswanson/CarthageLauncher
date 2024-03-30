/**
 * Interface for a transaction that can be performed and reverted on a [Transactional] object.
 *
 * A transaction represents a single unit of work that can be executed and then rolled back if necessary.
 * This interface defines two methods, `perform` and `revert`, that allow a transaction to be executed
 * and then undone, respectively.
 *
 * @template T The type of object that this transaction can be performed on.
 * @template R The type of the result returned by the `perform` method.
 *
 * @author thesurix
 */
interface Transaction<T, R> {
    /**
     * Performs this transaction on the given [transactional] object.
     *
    
