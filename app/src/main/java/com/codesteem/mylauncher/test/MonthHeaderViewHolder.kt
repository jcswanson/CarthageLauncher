package com.codesteem.mylauncher.test

import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.codesteem.mylauncher.databinding.MonthHeaderItemBinding
import com.codesteem.mylauncher.gesture.GestureViewHolder

/**
 * A custom ViewHolder for displaying month headers in the launcher.
 * This ViewHolder extends GestureViewHolder and overrides canDrag() and canSwipe() methods
 * to disable drag and swipe gestures for month headers.
 */
class MonthHeaderViewHolder(private val binding: MonthHeaderItemBinding) : GestureViewHolder<MonthItem>(binding.root) {

    // Initialize a TextView for displaying the month name
    private val headerText: TextView = binding.headerText

    init {
        // Disable drag and swipe gestures for month headers
        setGestureDetector(object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent?): Boolean {
                return true
            }

            override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
                return true
            }
        })
    }

    override fun canDrag(): Boolean {
        return false
    }

    override fun canSwipe(): Boolean {
        return false
    }

    /**
     * Binds the month item to the view holder and sets the month name text.
     */
    fun bind(item: MonthItem) {
        headerText.text = item.monthName
    }
}
