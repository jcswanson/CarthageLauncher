package com.codesteem.mylauncher.anothertest;

import android.graphics.drawable.Drawable;
import android.view.DragEvent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.codesteem.mylauncher.R;

import java.util.List;

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
            if (isTargetView(viewId, cvItem, tvEmptyListTop, tvEmptyListBottom, tvEmptyListMid, rvTop, rvBottom, rvMid)) {

                RecyclerView target = getTargetRecyclerView(viewId, view.getRootView());
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
                    updateEmptyLists(sourceId, adapterSource, listener);
                }
            }
        }

        if (!isDropped && event.getLocalState() != null) {
            ((View) event.getLocalState()).setVisibility(View.VISIBLE);
        }
        return true;
    }

    // Helper method to check if the target view is one of the RecyclerViews or the new views
    private boolean isTargetView(int viewId, int... targetViews) {
        for (int targetView : targetViews) {
            if (viewId == targetView) {
                return true;
            }
        }
        return false;
    }

    // Helper method to get the target RecyclerView based on the viewId
    private RecyclerView getTargetRecyclerView(int viewId, View rootView) {
        switch (viewId) {
            case R.id.cvItem:
            case R.id.tvEmptyListTop:
            case R.id.tvEmptyListBottom:
            case R.id.tvEmptyListMid:
                return (RecyclerView) rootView.findViewById(R.id.rvTop);
            case R.id.rvTop:
                return (RecyclerView) rootView.findViewById(R.id.rvTop);
            case R.id.rvBottom:
                return (RecyclerView) rootView.findViewById(R.id.rvBottom);
            case R.id.rvMid:
                return (RecyclerView) rootView.findViewById(R.id.rvMid);
            default:
                return null;
        }
    }

    // Helper method to update empty lists as needed
    private void updateEmptyLists(int sourceId, MainAdapter adapterSource, Listener listener) {
        if (sourceId == R.id.rvBottom && adapterSource.getItemCount() < 1) {
            listener.setEmptyListBottom(true);
