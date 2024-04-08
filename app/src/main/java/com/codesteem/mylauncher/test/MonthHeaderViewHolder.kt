package com.codesteem.mylauncher.test

import android.widget.TextView
import com.codesteem.mylauncher.databinding.MonthHeaderItemBinding
import com.codesteem.mylauncher.gesture.GestureViewHolder

/**
 * A custom ViewHolder for displaying month headers in the launcher.
 * This ViewHolder extends GestureViewHolder and overrides canDrag() and canSwipe() methods to disable
 * drag and swipe gestures for month headers.
 */
class MonthHeaderViewHolder(binding: MonthHeaderItemBinding) : GestureViewHolder<MonthItem>(binding.root) {

    // Initialize a TextView for displaying the month name
    private val headerText: TextView = binding.headerText

    /**

