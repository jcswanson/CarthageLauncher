package com.codesteem.mylauncher.anothertest;

import android.graphics.drawable.Drawable;
import android.view.DragEvent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.codesteem.mylauncher.R;

/**
 * A custom OnDragListener implementation that handles drag and drop events for RecyclerView items.
 */
public class DragListener implements View.OnDragListener {

    // Flag to track if an item has been dropped during the current drag event
    private boolean isDropped = false;
    
    // RecyclerView item drag and drop event listener
    private Listener listener;

    /**
     * Constructor to initialize the DragListener with a Listener.
     *
     * @param listener The Listener to be initialized.
     */
    public DragListener(Listener listener) {
        this.listener = listener;
    }

    /**
     * Callback method to be invoked when a drag event occurs.
     *
     * @param view    The View that the drag event is targeting.
     * @param event    The DragEvent object containing information about the drag event.
     * @return True if the listener has handled the event, false otherwise.
     */
    @Override
    public boolean onDrag(View view, DragEvent event) {
        if (event.getAction() == DragEvent.ACTION_DROP) {
            isDropped = true;
            int positionTarget = -1;

            // Get the source View and various RecyclerView IDs
            View viewSource = (View) event.getLocalState();
            int viewId = view.getId();
            final int cvItem = R.id.cvGrid;
            final int tvEmptyListTop = R.id.tvEmptyListTop;
            final int tvEmptyListBottom = R.id.tvEmptyListBottom;
            final int tvEmptyListMid = R.id.tvEmptyListMid; // New ID for tvEmptyListMid
            final int rvTop = R.id.rvTop;
            final int rvBottom = R.id.rvBottom;
            final int rvMid = R.id.rvMid; // New ID for rvMid

            // Check if the target View is one of the RecyclerViews or the new views
            if (viewId == cvItem || viewId == tvEmptyListTop || viewId == tvEmptyListBottom || viewId == tvEmptyListMid || viewId == rvTop || viewId == rvBottom || viewId == rvMid) { // Include the new views

                RecyclerView target;
                if (viewId == tvEmptyListTop || viewId == rvTop) {
                    target = (RecyclerView) view.getRootView().findViewById(rvTop);
                } else if (viewId == tvEmptyListBottom || viewId == rvBottom) {
                    target = (RecyclerView) view.getRootView().findViewById(rvBottom);
                } else if (viewId == tvEmptyListMid || viewId == rvMid) { // Handle the new views
                    target = (RecyclerView) view.getRootView().findViewById(rvMid);
                } else {
                    target = (RecyclerView) view.getParent();
                    positionTarget = (int) view.getTag();
                }

                if (viewSource != null) {
                    RecyclerView source = (RecyclerView) viewSource.getParent();

                    MainAdapter adapterSource = (MainAdapter) source.getAdapter();
                    int position = (int) viewSource.getTag();
                    int sourceId = source.getId();

                    Drawable list = adapterSource.getList().get(position);
                    List<Drawable> listSource = adapterSource.getList();

                    // Remove the item from the source list
                    listSource.remove(position);
                    adapterSource.updateList(listSource);
                    adapterSource.notifyDataSetChanged();

                    MainAdapter adapterTarget = (MainAdapter) target.getAdapter();
                    List<Drawable> customListTarget = adapterTarget.getList();

                    // Add the item to the target list
                    if (source == target) {
                        // Reorder the item within the same RecyclerView
                        if (positionTarget >= 0) {
                            customListTarget.add(positionTarget, list);
                        } else {
                            customListTarget.add(list);
                        }
                    } else {
                        // Add the item to the target list when dragging between RecyclerViews
                        if (positionTarget >= 0) {
                            customListTarget.add(positionTarget, list);
                        } else {
                            customListTarget.add(list);
                        }
                    }

                    adapterTarget.updateList(customListTarget);
                    adapterTarget.notifyDataSetChanged();

                    // Update empty lists as needed
                    if (sourceId == rvBottom && adapterSource.getItemCount() < 1) {
                        listener.setEmptyListBottom(true);
                    }
                    if (sourceId == rvMid && adapterSource.getItemCount() < 1) {
                        listener.setEmptyListMid(true);
                    }
                    if (viewId == tvEmptyListBottom) {
                        listener.setEmptyListBottom(false);
                    }
                    if (viewId == tvEmptyListMid) {
                        listener.setEmptyListMid(false);
                    }
                    if (sourceId == rvTop && adapterSource.getItemCount() < 1) {
                        listener.setEmptyListTop(true);
                    }
                    if (viewId == tvEmptyListTop) {
                        listener.setEmptyListTop(false);
                    }
                }
            }
        }

        if (!isDropped && event.getLocalState() != null) {
            ((View) event.getLocalState()).setVisibility(View.VISIBLE);
        }
        return true;
    }

}
