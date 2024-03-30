// com.codesteem.mylauncher package

/**
 * Interface for listening to changes in the user's selection.
 *
 * Implementors of this interface will receive notifications when the user's selection changes,
 * including the number of items selected and the package name of the selected items.
 */
interface SelectionChangeListener {

    /**
     * Called when the user's selection has changed.
     *
     * @param selectedCount The number of items currently selected.
     * @param packageName The package name of the selected items.
     */
    fun onSelectionChanged(selectedCount: Int, packageName: String)
}
