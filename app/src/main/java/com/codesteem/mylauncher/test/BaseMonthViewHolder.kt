package com.codesteem.mylauncher.test

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

abstract class BaseMonthViewHolder(
    private val rootView: View
) : GestureViewHolder<MonthItem>(rootView) {
    protected abstract val tvCounter: TextView
    protected abstract val monthText: TextView
    protected abstract val monthPicture: ImageView
    protected abstract val itemDrag: ImageView
    protected abstract val foreground: View?

    override val draggableView: View?
        get() = itemDrag

    override val foregroundView: View
        get() = foreground ?: super.foregroundView

    override fun bind(item: MonthItem) {
        if (item is Month) {
            monthText.text = item.name
            if (item.counterNotification!=null&&item.counterNotification!=0){
                tvCounter.visibility=View.VISIBLE
                tvCounter.text = (item.counterNotification).toString()
            }else{
                tvCounter.visibility=View.GONE

            }
            Glide.with(itemView.context)
                    .load(item.drawableId)
                    .apply(RequestOptions.centerCropTransform())
                    .into(monthPicture)
        }
    }

    override fun onItemSelect() {
        rootView.setOnDragListener(DragListener())
        val textColorFrom = ContextCompat.getColor(itemView.context, android.R.color.white)
        val textColorTo = ContextCompat.getColor(itemView.context, R.color.black)
        ValueAnimator.ofObject(ArgbEvaluator(), textColorFrom, textColorTo).apply {
            duration = itemView.context.resources.getInteger(R.integer.animation_speed_ms).toLong()
            addUpdateListener(getTextAnimatorListener(monthText, this))
            start()
        }


        val backgroundColorFrom = ContextCompat.getColor(itemView.context, R.color.black)
        val backgroundColorTo = ContextCompat.getColor(itemView.context, android.R.color.white)
        ValueAnimator.ofObject(ArgbEvaluator(), backgroundColorFrom, backgroundColorTo).apply {
            duration = itemView.context.resources.getInteger(R.integer.animation_speed_ms).toLong()
            addUpdateListener(getBackgroundAnimatorListener(monthText, this))
            start()
        }
    }

    override fun onItemClear() {
        val textColorFrom = ContextCompat.getColor(itemView.context, R.color.black)
        val textColorTo = ContextCompat.getColor(itemView.context, android.R.color.white)
        ValueAnimator.ofObject(ArgbEvaluator(), textColorFrom, textColorTo).apply {
            duration = itemView.context.resources.getInteger(R.integer.animation_speed_ms).toLong()
            addUpdateListener(getTextAnimatorListener(monthText, this))
            start()
        }


        val backgroundColorFrom = ContextCompat.getColor(itemView.context, android.R.color.white)
        val backgroundColorTo = ContextCompat.getColor(itemView.context, R.color.black)
        ValueAnimator.ofObject(ArgbEvaluator(), backgroundColorFrom, backgroundColorTo).apply {
            duration = itemView.context.resources.getInteger(R.integer.animation_speed_ms).toLong()
            addUpdateListener(getBackgroundAnimatorListener(monthText, this))
            start()
        }
    }

    override fun canDrag() = true

    override fun canSwipe() = true

    private fun getBackgroundAnimatorListener(view: TextView, animator: ValueAnimator): ValueAnimator.AnimatorUpdateListener {
        return ValueAnimator.AnimatorUpdateListener { view.setBackgroundColor(animator.animatedValue as Int) }
    }

    private fun getTextAnimatorListener(view: TextView, animator: ValueAnimator): ValueAnimator.AnimatorUpdateListener {
        return ValueAnimator.AnimatorUpdateListener { view.setTextColor(animator.animatedValue as Int) }
    }
}