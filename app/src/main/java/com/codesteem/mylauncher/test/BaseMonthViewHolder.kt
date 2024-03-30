// BaseMonthViewHolder is an abstract class that extends GestureViewHolder and provides basic functionality for displaying and managing MonthItem objects.
package com.codesteem.mylauncher.test

// Import necessary Android libraries and custom classes.
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.codesteem.mylauncher.R
import com.codesteem.mylauncher.adapter.DragListener
import com.codesteem.mylauncher.gesture.GestureViewHolder

// Define the BaseMonthViewHolder class with a constructor that takes a rootView parameter.
abstract class BaseMonthViewHolder(
    private val rootView: View
) : GestureViewHolder<MonthItem>(rootView) {

    // Declare abstract properties that must be implemented by the subclasses.
    protected abstract val tvCounter: TextView
    protected abstract val monthText: TextView
    protected abstract val monthPicture: ImageView
    protected abstract val itemDrag: ImageView
    protected abstract val foreground: View?

    // Override the draggableView property to return the itemDrag ImageView.
    override val draggableView: View?
        get() = itemDrag

    // Override the foregroundView property to return the foreground View or the superclass's foregroundView if it's null.
    override val foregroundView: View
        get() = foreground ?: super.foregroundView

    // Implement the bind() method to set the properties of the Views based on the provided MonthItem.
    override fun bind(item: MonthItem) {
        if (item is Month) { // Check if the item is an instance of Month.
            monthText.text = item.name // Set the monthText's text to the item's name.
            if (item.counterNotification!=null&&item.counterNotification!=0){ // If the item has a non-null and non-zero counterNotification.
                tvCounter.visibility=View.VISIBLE // Make the counter visible.
                tvCounter.text = (item.counterNotification).toString() // Set the counter's text to the counterNotification value.
            }else{
                tvCounter.visibility=View.GONE // Otherwise, hide the counter.

            }
            Glide.with(itemView.context) // Use Glide to load the item's drawable into the monthPicture ImageView.
                    .load(item.drawableId)
                    .apply(RequestOptions.centerCropTransform())
                    .into(monthPicture)
        }
    }

    // Implement the onItemSelect() method to change the appearance of the Views when the item is selected.
    override fun onItemSelect() {
        rootView.setOnDragListener(DragListener()) // Set the rootView's OnDragListener to the DragListener.
        val textColorFrom = ContextCompat.getColor(itemView.context, android.R.color.white) // Define the starting text color.
        val textColorTo = ContextCompat.getColor(itemView.context, R.color.black) // Define the ending text color.
        ValueAnimator.ofObject(ArgbEvaluator(), textColorFrom, textColorTo).apply { // Create a ValueAnimator that changes the text color.
            duration = itemView.context.resources.getInteger(R.integer.animation_speed_ms).toLong() // Set the duration of the animation.
            addUpdateListener(getTextAnimatorListener(monthText, this)) // Add a listener to update the text color when the animation updates.
            start() // Start the animation.
        }


        val backgroundColorFrom = ContextCompat.getColor(itemView.context, R.color.black) // Define the starting background color.
        val backgroundColorTo = ContextCompat.getColor(itemView.context, android.R.color.white) // Define the ending background color.
        ValueAnimator.ofObject(ArgbEvaluator(), backgroundColorFrom, backgroundColorTo).apply { // Create a ValueAnimator that changes the background color.
            duration = itemView.context.resources.getInteger(R.integer.animation_speed_ms).toLong() // Set the duration of the animation.
            addUpdateListener(getBackgroundAnimatorListener(monthText, this)) // Add a listener to update the background color when the animation updates.
            start() // Start the animation.
        }
    }

    // Implement the onItemClear() method to change the appearance of the Views when the item is cleared.
    override fun onItemClear() {
        val textColorFrom = ContextCompat.getColor(itemView.context, R.color.black) // Define the starting text color.
        val textColorTo = ContextCompat.getColor(itemView.context, android.R.color.white) // Define the ending text color.
        ValueAnimator.ofObject(ArgbEvaluator(), textColorFrom, textColorTo).apply { // Create a ValueAnimator that changes the text color.
            duration = itemView.context.resources.getInteger(R.integer.animation_speed_ms).toLong() // Set the duration of the animation.
            addUpdateListener(getTextAnimatorListener(monthText, this)) // Add a listener to update the text color when the animation updates.
            start() // Start the animation.
        }


        val backgroundColorFrom = ContextCompat.getColor(itemView.context, android.R.color.white) // Define the starting background color.
        val backgroundColorTo = ContextCompat.getColor(itemView.context, R.color.black) // Define the ending background color.
        ValueAnimator.ofObject(ArgbEvaluator(), backgroundColorFrom, backgroundColorTo).apply { // Create a ValueAnimator that changes the background color.
            duration = itemView.context.resources.getInteger(R.integer.animation_speed_ms).toLong() // Set the duration of the animation.
            addUpdateListener(getBackgroundAnimatorListener(monthText, this)) // Add a listener to update the background color when the animation updates.
            start()
