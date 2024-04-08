package com.codesteem.mylauncher

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.codesteem.mylauncher.databinding.LoadingDialogBinding

/**
 * A custom loading dialog class for displaying a loading animation with an optional title.
 * Users can set an OnActionListener to handle the OK and Cancel button clicks.
 */
class LoadingDialog(
    context: Context, // Context of the calling component
    private val title: String? // Optional title for the dialog
) : Dialog(context) {
    private lateinit var binding: LoadingDialogBinding // View binding for the dialog layout
    private var mListener: OnActionListener? = null // Listener for button clicks

    /**
     * Dialog creation method, inflates the layout, sets the content view, and configures the dialog window.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoadingDialogBinding.inflate(layoutInflater) // Inflate the dialog layout
        requestWindowFeature(Window.FEATURE_NO_TITLE) // Remove the default dialog title
        setContentView(binding.root) // Set the content view using the inflated layout

        // Calculate and set the dialog width to 90% of the screen width
        val width = context.resources.displayMetrics.widthPixels * 0.90
        binding.root.layoutParams.width = width.toInt()

        // Set the title if provided
        if (title != null) {
            binding.title.text = title
        }

        // Transparent background for the dialog window
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    /**
     * Interface for handling button clicks in the dialog
     */
    interface OnActionListener {
        fun onOK() // Method for handling the OK button click
        fun onCancel() // Method for handling the C
