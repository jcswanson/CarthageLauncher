package com.codesteem.mylauncher.test

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.codesteem.mylauncher.databinding.GridItemBinding

// Custom ViewHolder for the grid items in the calendar view
class GridItemViewHolder(private val binding: GridItemBinding) : BaseMonthViewHolder(binding.root) {

    override val monthText: TextView
        get() = binding.monthText

    override val monthPicture: ImageView
        get() = binding.monthImage

    override val itemDrag: ImageView
        get() = binding.monthDrag

    override val tvCounter: TextView
        get() = binding.tvCounter

    override val foreground: View?
        get() = binding.root // Returns the root view as the foreground view
}
