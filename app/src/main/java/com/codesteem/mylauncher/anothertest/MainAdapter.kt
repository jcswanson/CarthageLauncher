package com.codesteem.mylauncher.anothertest

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.DragShadowBuilder
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.codesteem.mylauncher.anothertest.MainAdapter.ListViewHolder
import com.codesteem.mylauncher.databinding.ListItemMainBinding

/**
 * Adapter for the main RecyclerView, which displays the grid of items.
 * 
 * Created by Usman on 19-09-2020
 */

class MainAdapter(var list: List<Drawable>,
                  private val listener: Listener?,private val context: Context) : RecyclerView.Adapter<ListViewHolder>(), OnTouchListener {

    // Inflates the layout for each item in the RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ListItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    /**
     * Binds the data to the views in the ViewHolder.
     * Loads the drawable into the ImageView using Glide.
     * Sets the onTouchListener and onDragListener for the CardView.
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        Glide.with(context).load(list[position]).into(holder.ivGrid)

        holder.cvGrid.tag = position
        holder.cvGrid.setOnTouchListener(this)
        holder.cvGrid.setOnDragListener(DragListener(listener))
    }

    // Returns the size of the list
    override fun getItemCount(): Int {
        return list.size
    }

    // Returns the position of the item in the list
    override fun getItemViewType(position: Int): Int {
        return position
    }

    /**
     * Handles the touch event for the CardView.
     * Starts a drag event when the user touches down on the CardView.
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val data = ClipData.newPlainText("", "")
            val shadowBuilder = DragShadowBuilder(view)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                view.startDragAndDrop(data, shadowBuilder, view, 0)
            } else {
                view.startDrag(data, shadowBuilder, view, 0)
            }
            return true
        }
        return false
    }

    /**
     * Updates the list of drawables displayed in the RecyclerView.
     */
    fun updateList(list: List<Drawable>) {
        this.list = list
    }

    /**
     * Returns the DragListener instance, which handles the drag events for the CardView.
     */
    val dragInstance: DragListener?
        get() = if (listener != null) {
            DragListener(listener)
        } else {
            null
        }

    /**
     * ViewHolder for the items in the RecyclerView.
     * Contains an ImageView for the drawable and a CardView for the item.
     */
    inner class ListViewHolder(binding: ListItemMainBinding) : ViewHolder(binding.root) {
        var ivGrid: ImageView
        var cvGrid: CardView

        init {
            ivGrid = binding.ivGrid
            cvGrid = binding.cvGrid
        }
    }
}
