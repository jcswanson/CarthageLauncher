/**
 * A callback for the draggable view to update its visibility based on the drag event.
 * This class listens for drag events and hides the view when a drag has started
 * (`DragEvent.ACTION_DRAG_ENTERED`) and shows the view again when the drag has ended
 * (`DragEvent.ACTION_DRAG_ENDED`).
 */
class DragListener : View.OnDragListener {

    override fun onDrag(view: View, dragEvent: DragEvent): Boolean {
        when (dragEvent.action) {
            DragEvent.ACTION_DRAG_ENTERED -> handleDragEntered(view)
            DragEvent.ACTION_DRAG_ENDED -> handleDragEnded(view)
        }
        return true
    }

    private fun handleDragEntered(view: View) {
        view.visibility = View.INVISIBLE
    }

    private fun handleDragEnded(view: View) {
        view.visibility = View.VISIBLE
    }
}
