package com.codesteem.mylauncher.test

import android.widget.TextView
import com.codesteem.mylauncher.databinding.MonthHeaderItemBinding
import com.codesteem.mylauncher.gesture.GestureViewHolder


class MonthHeaderViewHolder(binding: MonthHeaderItemBinding) : GestureViewHolder<MonthItem>(binding.root) {

    private val headerText: TextView = binding.headerText

    override fun canDrag() = false

    override fun canSwipe() = false

    override fun bind(item: MonthItem) {
        headerText.text = item.name
    }
}
