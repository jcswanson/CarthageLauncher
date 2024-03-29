package com.codesteem.mylauncher.util

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable

class CustomDrawable : Drawable() {

    private val paint: Paint = Paint()

    init {
        paint.color = Color.TRANSPARENT // Set your desired color
        paint.alpha = 128 // Set the alpha value for transparency (0-255)
    }

    override fun draw(canvas: Canvas) {
        // Draw your custom content here
        // For example, draw a circle with the paint settings
        val centerX = bounds.centerX()
        val centerY = bounds.centerY()
        val radius = Math.min(centerX, centerY)

        canvas.drawCircle(centerX.toFloat(), centerY.toFloat(), radius.toFloat(), paint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: android.graphics.ColorFilter?) {
        // Not needed for a simple Drawable
    }

    override fun getOpacity(): Int {
        // Define the opacity of your Drawable
        return android.graphics.PixelFormat.TRANSLUCENT
    }
}
