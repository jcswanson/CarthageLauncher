/**
 * This interface defines a contract for a listener that can be used to show or hide various
 * empty list views in the UI.
 */
interface Listener {

    /**
     * Sets the visibility of the empty list top view.
     *
     * @param visibility `true` to show the empty list top view, `false` to hide it.
     */
    fun setEmptyListTop(visibility: Boolean)

    /**
     * Sets the visibility of the empty list bottom view.
     *
     * @param visibility `true` to show the empty list bottom view, `false` to hide it.
     */
    fun setEmptyListBottom(visibility: Boolean)

    /**
     * Sets the visibility of the empty list middle view.
     *
     * @param visibility `true` to show the empty list middle view, `false` to hide it.
     */
    fun setEmptyListMid(visibility: Boolean)
}
