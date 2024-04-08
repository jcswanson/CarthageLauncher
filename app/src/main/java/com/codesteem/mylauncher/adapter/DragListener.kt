/**
 * A callback for the draggable view to update its visibility based on the drag event.
 * This class listens for drag events and hides the view when a drag has started
 * (`DragEvent.ACTION_DRAG_ENTERED`) and shows the view again when the drag has ended
 * (`DragEvent.ACTION_DRAG_ENDED`).
 */
class DragListener : View.OnDragListener {
  /**
   * Called when a drag event is in progress. This method is responsible for handling the drag
   * event and updating the view's visibility accordingly.
   *
   * @param view The view that is being dragged.
   * @param dragEvent The drag event that is currently in progress.
   * @return True if the drag event was handled, false otherwise.
   */
  override fun onDrag(view: View, dragEvent: DragEvent): Boolean {
    when (dragEvent.action) {
      // Hide the view when the drag event enters the view.
      DragEvent.ACTION_DRAG_ENTERED -> view.visibility = View.INVISIBLE

      // Show the view again when the drag event has ended.
      DragEvent.ACTION_DRAG_ENDED -> view.visibility = View.VISIBLE
    }

    // Return true to indicate that the drag event was handled.
    return true
  }
}
