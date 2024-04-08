/**
 * A [Transaction] implementation that swaps the elements at the given [firstIndex] and [secondIndex] positions in a [Transactional] data collection.
 *
 * This class is parameterized with the type of elements in the data collection (T).
 *
 * @author thesurix
 * @property firstIndex The index of the first element to be swapped.
 * @property secondIndex The index of the second element to be swapped.
 * @property headerEnabled A flag indicating whether the data collection has a header row. If true, the offset of the changed indices will be increased by 1 to account for the header row.
 */
class SwapTransaction<T>(private val firstIndex: Int,
                         private val secondIndex: Int,

