package com.codesteem.mylauncher.test

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.codesteem.mylauncher.databinding.GridItemBinding

// Custom ViewHolder for the grid items in the calendar view
class GridItemViewHolder (
    private val binding: GridItemBinding // GridItemBinding instance for data binding
) : BaseMonthViewHolder(binding.root) {

    // Overriding the 'monthText' property from the BaseMonthViewHolder
    override val monthText: TextView
        get() = binding.monthText // Returns the TextView for displaying the month name

    // Overriding the 'monthPicture' property from the BaseMonthViewHolder
    override val monthPicture: ImageView
        get() = binding.monthImage // Returns the ImageView for displaying the month picture

    // Overriding the 'itemDrag' property from the BaseMonthViewHolder
    override val itemDrag: ImageView
        get() = binding.monthDrag // Returns the ImageView for handling the drag event

    // Overriding the 'tvCounter' property from the BaseMonthViewHolder
    override val tvCounter: TextView
        get() = binding.tvCounter // Returns the TextView for displaying the counter

    // Overriding the 'foreground' property from the BaseMonthViewHolder
    override val foreground: View?
        get() = null // Returns null as there's no specific foreground view for this ViewHolder
}
