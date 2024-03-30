// This interface defines the contract for an adapter that can handle item swipes and moves
// in a RecyclerView using the ItemTouchHelper class.

// The onItemSwiped() method is called when an item is swiped in the RecyclerView.
// The position parameter indicates the index of the swiped item, while the direction
// parameter indicates the direction of the swipe (either ItemTouchHelper.LEFT or
// ItemTouchHelper.RIGHT). The viewHolder parameter is the ViewHolder object that
// represents the swiped item.
interface ItemTouchHelperAdapter {
    // Called when an item is swiped in the RecyclerView.
    fun onItemSwiped(position: Int, direction: Int, viewHolder: RecyclerView.ViewHolder)

    // The onItemMoved() method is called when an item is moved in the RecyclerView.
    // The fromPosition parameter indicates the index of the item that was moved,
    // while the toPosition parameter indicates the index of the new position.
    fun onItemMoved(fromPosition: Int, toPosition: Int)
}
