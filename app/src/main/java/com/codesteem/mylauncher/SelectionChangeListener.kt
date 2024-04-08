// Interface for listening to changes in user selection within the app.
// Implementing classes should provide an implementation for the `onSelectionChanged` method.
interface SelectionChangeListener {

    // Called when the user's selection has changed within the app.
    //  `selectedCount` represents the number of items currently selected,
    //  while `packageName` identifies the package or context in which the selection occurred.
    fun onSelectionChanged(selectedCount: Int, packageName: String)
}
