package com.codesteem.mylauncher.adapter

import android.animation.ValueAnimator
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
      // When the dragged item enters the drop area, animate and highlight a background under the view
      DragEvent.ACTION_DRAG_ENTERED -> {
        // Get the background color from the resources
        val bgColor = ContextCompat.getColor(view.context, R.color.white)

        // If the view already has the correct background color, return true without doing anything
        if (view.background is ColorDrawable && (view.background as ColorDrawable).color == bgColor) return true

        // Animate the background color change
        ValueAnimator.ofArgb(Color.TRANSPARENT, bgColor).apply {
          addUpdateListener {
            val color = it.animatedValue as Int
            view.setBackgroundColor(color)
          }
          duration = 500
        }.start()
      }
      // When the dragged item leaves the drop area, animate and hide the highlight under the view
      DragEvent.ACTION_DRAG_ENDED -> {
        // Get the background color from the resources
        val bgColor = ContextCompat.getColor(view.context, R.color.white)

        // If the view already has the correct background color, return true without doing anything
        if (view.background is ColorDrawable && (view.background as ColorDrawable).color == Color.TRANSPARENT) return true

        // Animate the background color change
        ValueAnimator.ofArgb(bgColor, Color.TRANSPARENT).apply {
          addUpdateListener {
            val color = it.animatedValue as Int
            view.setBackgroundColor(color)
          }
          duration = 500
        }.start()
      }
      // When an item is dropped, notify the listener about it
      DragEvent.ACTION_DROP -> onDrop()
    }

    // Return true to indicate that the drag event was
