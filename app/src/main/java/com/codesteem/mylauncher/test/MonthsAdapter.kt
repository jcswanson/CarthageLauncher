package com.codesteem.mylauncher.test

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codesteem.mylauncher.R
import com.codesteem.mylauncher.databinding.GridItemBinding
import com.codesteem.mylauncher.databinding.MonthHeaderItemBinding
import com.codesteem.mylauncher.gesture.GestureAdapter
import com.codesteem.mylauncher.gesture.GestureViewHolder
import com.codesteem.mylauncher.gesture.TYPE_HEADER_ITEM
import com.codesteem.mylauncher.model.MonthItem

class MonthsAdapter(
    @LayoutRes private val mItemResId: Int,
    private val onItemClick: (MonthItem) -> Unit
) : ListAdapter<MonthItem, RecyclerView.ViewHolder>(MonthItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == MonthItem.MonthItemType.MONTH.ordinal) {
            GridItemViewHolder(
                GridItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        } else if (viewType == TYPE_HEADER_ITEM) {
            createHeaderViewHolder(parent.context, parent)
        } else {
            MonthHeaderViewHolder(MonthHeaderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position).let { item ->
            when (holder) {
                is GridItemViewHolder -> holder.bind(item)
                is MonthHeaderViewHolder -> holder.bind(item)
            }
            holder.itemView.setOnClickListener { onItemClick(item) }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type.ordinal
    }

    private fun createHeaderViewHolder(context: Context, parent: ViewGroup): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.header_item, parent, false)
        return object : RecyclerView.ViewHolder(view) {
            override fun canDrag() = false

            override fun canSwipe() = false
        }
    }

    fun addItem(selectedApp: MonthItem) {
        val newList = mutableListOf(*currentList.toTypedArray(), selectedApp)
        submitList(newList)
    }

    fun removeItem(selectedApp: MonthItem) {
        val newList = currentList.toMutableList().apply { remove(selectedApp) }
        submitList(newList)
    }

    fun moveItem(from: Int, to: Int) {
        val newList = currentList.toMutableList().apply {
            val fromLocation = get(from)
            removeAt(from)
            if (to < from) {
                add(to + 1, fromLocation)
            } else {
                add(to - 1, fromLocation)
            }
        }
        submitList(newList)
    }

    class MonthItemDiffCallback : DiffUtil.ItemCallback<MonthItem>() {
        override fun areItemsTheSame(oldItem: MonthItem, newItem: MonthItem): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: MonthItem, newItem: MonthItem): Boolean =
            oldItem == newItem
    }
}

abstract class MonthsViewHolder<T : MonthItem>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item: T)
    open fun canDrag() = true
    open fun canSwipe() = true
}

class GridItemViewHolder(private val binding: GridItemBinding) : MonthsViewHolder<MonthItem.Month>(binding.root) {
    override fun bind(item: MonthItem.Month) {
        binding.month = item
        binding.
