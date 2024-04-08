// Interface for RecyclerView.Adapter classes that support item drag and drop and swipe-to-dismiss gestures.
interface ItemTouchHelperAdapter {

    // Called when an item has been swiped.
    // 
    // @param position The position of the item that was swiped.
    // @param direction The direction of the swipe (either ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT).
    // @param viewHolder The ViewHolder for the swiped item.
    fun onItemSwiped(position: Int, direction: Int, viewHolder: RecyclerView.ViewHolder)

    // Called when an item has been moved.
    // 
    // @param fromPosition The original position of the item that was moved.
    // @param toPosition The new position of the item that was moved.
    fun onItemMoved(fromPosition: Int, toPosition: Int)
}
