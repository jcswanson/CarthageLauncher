package com.codesteem.mylauncher

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import androidx.annotation.StringRes
import com.codesteem.mylauncher.databinding.LoadingDialogBinding

/**
 * A custom loading dialog class for displaying a loading animation with an optional title.
 * Users can set an OnActionListener to handle the OK and Cancel button clicks.
 */
class LoadingDialog(
    context: Context,
    @StringRes titleResId: Int? // Optional title resource ID for the dialog
) : Dialog(context) {
    private lateinit var binding: LoadingDialogBinding // View binding for the dialog layout
    private var mListener: OnActionListener? = null // Listener for button clicks

    /**
     * Dialog creation method, inflates the layout, sets the content view, and configures the dialog window.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoadingDialogBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE) // Removes the default dialog title
        setContentView(binding.root)

        // Calculates the width of the dialog to be 90% of the screen width
        val width = context.resources.displayMetrics.widthPixels * 0.90
        binding.root.layoutParams.width = width.toInt()

        // Sets the background of the dialog window to be transparent
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Sets the title if provided
        titleResId?.let {
            binding.title.text = context.getString(it)
        }
    }

    /**
     * Interface for handling button clicks in the dialog
     */
    interface OnActionListener {
        fun onOK() // Method for handling the OK button click
        fun onCancel() // Method for handling the Cancel button click
    }

    /**
     * Setter for the OnActionListener
     *
     * @param listener The OnActionListener instance
     */
    fun setOnActionListener(listener: OnActionListener) {
        mListener = listener
    }
}
