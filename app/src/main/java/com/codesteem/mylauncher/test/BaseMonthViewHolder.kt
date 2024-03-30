import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.codesteem.mylauncher.R
import com.codesteem.mylauncher.adapter.DragListener
import com.codesteem.mylauncher.gesture.GestureViewHolder

abstract class BaseMonthViewHolder(
    private val rootView: View,
    protected val tvCounter: TextView,
    protected val monthText: TextView,
    protected val monthPicture: ImageView,
    protected val itemDrag: ImageView,
    protected val foreground: View?
) : GestureViewHolder<MonthItem>(rootView) {

    override val draggableView: View?
        get() = itemDrag

    override val foregroundView: View
        get() = foreground ?: super.foregroundView

    override fun bind(item: MonthItem) {
        if (item is Month) {
            monthText.text = item.name
            tvCounter.visibility = if (item.counterNotification != null && item.counterNotification != 0) View.VISIBLE else View.GONE
            tvCounter.text = item.counterNotification.toString()
            Glide.with(itemView.context)
                .load(item.drawableId)
                .apply(RequestOptions.centerCropTransform())
                .into(monthPicture)
        }
    }

    override fun onItemSelect() {
        rootView.setOnDragListener(DragListener())
        animateTextAndBackgroundColor(monthText, R.color.white, R.color.black)
    }

    override fun onItemClear() {
        animateTextAndBackgroundColor(monthText, R.color.black, R.color.white)
    }

    private fun animateTextAndBackgroundColor(view: View, startColor: Int, endColor: Int) {
        val textColorAnimator = ValueAnimator.ofObject(ArgbEvaluator(), ContextCompat.getColor(itemView.context, startColor), ContextCompat.getColor(itemView.context, endColor))
        textColorAnimator.interpolator = LinearInterpolator()
        textColorAnimator.duration = itemView.context.resources.getInteger(R.integer.animation_speed_ms).toLong()
        textColorAnimator.addUpdateListener { animator -> view.setBackgroundColor(animator.animatedValue as Int) }
        textColorAnimator.start()

        val backgroundColorAnimator = ValueAnimator.ofObject(ArgbEvaluator(), ContextCompat.getColor(itemView.context, endColor), ContextCompat.getColor(itemView.context, startColor))
        backgroundColorAnimator.interpolator = LinearInterpolator()
        backgroundColorAnimator.duration = itemView.context.resources.getInteger(R.integer.animation_speed_ms).toLong()
        backgroundColorAnimator.addUpdateListener { animator -> view.setBackgroundColor(animator.animatedValue as Int) }
        backgroundColorAnimator.start()
    }
}
