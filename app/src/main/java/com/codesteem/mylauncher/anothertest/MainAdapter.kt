package com.codesteem.mylauncher.anothertest

import android.annotation.SuppressLint
import android.content.ClipData
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codesteem.mylauncher.databinding.ListItemMainBinding

class MainAdapter(private val listener: Listener?) :
    ListAdapter<Drawable, MainAdapter.ListViewHolder>(DiffCallback()) {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ListItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return ListViewHolder(binding)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        getItem(position)?.let { drawable ->
            Glide.with(context).load(drawable).into(holder.ivGrid)
            holder.cvGrid.tag = position
            holder.cvGrid.setOnTouchListener(holder)
            holder.cvGrid.setOnDragListener(DragListener(listener))
        }
    }

    class ListViewHolder(private val binding: ListItemMainBinding) : RecyclerView.ViewHolder(binding.root), View.OnTouchListener {

        val ivGrid: ImageView = binding.ivGrid
        val cvGrid: CardView = binding.cvGrid

        init {
            itemView.setOnTouchListener(this)
        }

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
    }

    class DiffCallback : DiffUtil.ItemCallback<Drawable>() {

        override fun areItemsTheSame(oldItem: Drawable, newItem: Drawable): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Drawable, newItem: Drawable): Boolean {
            return oldItem.constantState == newItem.constantState
        }
    }

    fun updateList(list: List<Drawable>) {
        submitList(list)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    class DragListener(private val listener: Listener?) : View.OnDragListener {

        override fun onDrag(view: View, event: DragEvent): Boolean {
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> return true
                DragEvent.ACTION_DRAG_ENTERED -> view.alpha = 0.5f
                DragEvent.ACTION_DRAG_EXITED -> view.alpha = 1.0f
                DragEvent.ACTION_DROP -> {
                    val item = event.clipData.getItemAt(0).text.toString().toInt()
                    listener?.onItemDropped(item, view)
                    view.alpha = 1.0f
                    return true
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    view.alpha = 1.0f
                    return true
                }
                else -> return false
            }
        }
    }

    interface Listener {
        fun onItemDropped(item: Int, view: View)
    }
}
