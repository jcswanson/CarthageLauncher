package com.codesteem.mylauncher.util

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable

/**
 * CustomDrawable class extends Drawable and provides custom drawing functionality.
 */
class CustomDrawable : Drawable() {

    /**
     * The Paint object used to draw custom content on the Canvas.
     */
    private val paint: Paint = Paint()

    /**
     * Initializes the CustomDrawable instance with a transparent color and 50% transparency.
     */
    init {
        paint.color = Color.TRANSPARENT // Set your desired color
        paint.alpha = 128 // Set the alpha value for transparency (0-255)
    }

    /**
     * Draws custom content on the provided Canvas.
     * In this example, a circle is drawn with the paint settings.
     *
     * @param canvas The Canvas to draw on.
     */
    override fun draw(canvas: Canvas) {
        // Draw your custom content here
        // For example, draw a circle with the paint settings
        val centerX = bounds.centerX()
        val centerY = bounds.centerY()
        val radius = Math.min(centerX, centerY)

        canvas.drawCircle(centerX.toFloat(), centerY.toFloat(), radius.toFloat(), paint)
    }

    /**
     * Sets the alpha value of the Paint object.
     *
     * @param alpha The alpha value (0-255).
     */
    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    /**
     * Sets the color filter for the Paint object.
     * Not needed for a simple Drawable.
     *
     * @param colorFilter The ColorFilter to apply.
     */
    override fun setColorFilter(colorFilter: android.graphics.ColorFilter?) {
        // Not needed for a simple Drawable
    }

    /**
     * Returns the opacity
