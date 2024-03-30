package com.codesteem.mylauncher.adapter

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.DragEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.codesteem.mylauncher.R

/**
 * Callback for the target view where dragged items will be dropped
 * This class handles the animations and logic for entering, ending, and dropping the dragged items
 */
class DropListener(private val onDrop: () -> Unit) : View.OnDragListener {

    private val bgColor by lazy { ContextCompat.getColor(view.context, R.color.white) }

    /**
     * Called when a drag event is dispatched to this view. This method is responsible for handling
     * the drag event by inspecting the action type and performing the appropriate action.
     *
     * @param view The view to which the drag event has been dispatched.
     * @param dragEvent The drag event.
     * @return True if the drag event was handled, false otherwise.
     */
    override fun onDrag(view: View, dragEvent: DragEvent): Boolean {
        when (dragEvent.action) {
            DragEvent.ACTION_DRAG_ENTERED -> animateBackground(dragEvent, true)
            DragEvent.ACTION_DRAG_ENDED -> animateBackground(dragEvent, false)
            DragEvent.ACTION_DROP -> onDrop()
        }
        return true
    }

    private fun animateBackground(dragEvent: DragEvent, show: Boolean) {
        val targetColor = if (show) bgColor else Color.TRANSPARENT
        val currentColor = if (view.background is ColorDrawable) (view.background as ColorDrawable).color else Color.TRANSPARENT

        ValueAnimator.ofArgb(currentColor, targetColor).apply {
            addUpdateListener {
                val color = it.animatedValue as Int
                view.setBackgroundColor(color)
            }
            duration = 500
        }.start()
    }
}
