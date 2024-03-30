/**
 * This interface defines a contract for a listener that can be used to show or hide empty list views.
 */
interface EmptyListListener {

    /**
     * Sets the visibility of the empty list view.
     *
     * @param viewName The name of the empty list view to show or hide.
     * @param visibility `true` to show the empty list view, `false` to hide it.
     */
    fun setEmptyListView(viewName: String, visibility: Boolean)
}
