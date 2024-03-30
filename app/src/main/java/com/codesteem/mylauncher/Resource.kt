/**
 * A sealed class representing the different states of a [Resource].
 *
 * A `Resource` can be in one of four states:
 * 1. [Success]: The operation was successful and the data is available.
 * 2. [Error]: The operation failed and an error message is available.
 * 3. [Loading]: The operation is currently in progress.
 * 4. [Initial]: The `Resource` has not yet been initialized.
 *
 * @param T The type of data associated with this `Resource`.
 * @property data The data associated with this `Resource`, if available.
 * @property message An error message associated with this `Resource`, if available.
 */
sealed class Resource<out T>(val data: T? = null, val message: String? = null) {
    /**
     * A `Resource` in the [Success] state represents a successful operation.
     *
     * @property data The data associated with this `Resource`.
     */
    class Success<T>(data: T?) : Resource<T>(data)

    /**
     * A `Resource` in the [Error] state represents a failed operation.
     *
     * @property message The error message associated with this `Resource`.
     * @property data The data associated with this `Resource`, if available.
     */
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)

    /**
     * A `Resource` in the [Loading] state represents an operation in progress.
     */
    class Loading<T>() : Resource<T>()

    /**
     * A `Resource` in the [Initial] state represents an uninitialized `Resource`.
     */
    class Initial<T>() : Resource<T>()
}
