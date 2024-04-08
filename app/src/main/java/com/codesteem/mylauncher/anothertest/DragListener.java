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
    
    // Listener for drag events
    private Listener listener;

    /**
     * Constructs a DragListener instance with the given Listener.
     *
     * @param listener The Listener to be notified of drag events.
     */
    public DragListener(Listener listener) {
        this.listener = listener;
    }

    /**
     * Handles drag events for a view.
     *
     * @param view     The view for which the drag event is being dispatched.
     * @param event    The drag event.
     * @return True if the drag event was handled, false otherwise.
     */
    @Override
    public boolean onDrag(View view, DragEvent event) {
        if (event.getAction() == DragEvent.ACTION_DROP) {
            isDropped = true;
            int positionTarget = -1;

            // View IDs for the source and target views
            final int cvItem = R.id.cvGrid;
            final int tvEmptyListTop = R.id.tvEmptyListTop;
            final int tvEmptyListBottom = R.id.tvEmptyListBottom;
            final int tvEmptyListMid = R.id.tvEmptyListMid; // New ID for tvEmptyListMid
            final int rvTop = R.id.rvTop;
            final int rvBottom = R.id.rvBottom;
            final int rvMid = R.id.rvMid; // New ID for rvMid

            // Check if the event occurred over a valid target view
            if (view.getId() == cvItem || view.getId() == tvEmptyListTop || view.getId() == tvEmptyListBottom || view.getId() == tvEmptyListMid || view.getId() == rvTop || view.getId() == rvBottom || view.getId() == rvMid) { // Include the new views

                RecyclerView target;
                if (view.getId() == tvEmptyListTop || view.getId() == rvTop) {
                    target = (RecyclerView) view.getRootView().findViewById(rvTop);
                } else if (view.getId() == tvEmptyListBottom || view.getId() == rvBottom) {
                    target = (RecyclerView) view.getRootView().findViewById(rvBottom);
                } else if (view.getId() == tvEmptyListMid || view.getId() == rvMid) { // Handle the new views
                    target = (RecyclerView) view.getRootView().findViewById(rvMid);
                } else {
                    target = (RecyclerView) view.getParent();
                    positionTarget = (int) view.getTag();
                }

                if (event.getLocalState() != null) {
                    View viewSource = (View) event.getLocalState();
                    RecyclerView source = (RecyclerView) viewSource.getParent();

                    MainAdapter adapterSource = (MainAdapter) source.getAdapter();
                    int positionSource = (int) viewSource.getTag();
                    int sourceId = source.getId();

                    Drawable list = adapterSource.getList().get(positionSource);
                    List<Drawable> listSource = adapterSource.getList();

                    // Remove the item from the source list
                    listSource.remove(positionSource);
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
                    if (view.getId() == tvEmptyListBottom) {
                        listener.setEmptyListBottom(false);
                    }
                    if (view.getId() == tvEmptyListMid) {
                        listener.setEmptyListMid(false);
                    }
                    if (sourceId == rvTop && adapterSource.getItemCount() < 1) {
                        listener.setEmptyListTop(true);
                    }
                    if (view.getId() == tvEmptyListTop) {
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

    /**
     * Interface for listening to drag events.
    
