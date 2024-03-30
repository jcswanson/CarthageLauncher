// Interface for listeners that want to be notified when the selection hidden state changes.
interface SelectionHiddenChangeListener {

    // Called when the number of selected items and the package name have changed.
    // This method should be implemented by the listener to handle the change in selection hidden state.
    //
    // @param selectedCount The new number of selected items.
    // @param packageName The name of the package where the change occurred.
    fun onSelectionHiddenChanged(selectedCount: Int, packageName: String)
}
