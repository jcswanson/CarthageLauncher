package com.codesteem.mylauncher

/**
 * A function that is called when the user's selection has changed.
 *
 * @param selectedCount The number of items currently selected.
 * @param packageName The package name of the selected items.
 */
typealias SelectionChangeListener = (selectedCount: Int, packageName: String) -> Unit



val listener: SelectionChangeListener = { selectedCount, packageName ->
    // Do something when the selection changes
}



listener(5, "com.example.app")

