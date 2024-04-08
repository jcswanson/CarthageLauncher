package com.codesteem.mylauncher.bug_report

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.codesteem.mylauncher.databinding.ConfirmDialogBinding
import com.codesteem.mylauncher.databinding.LoadingDialogBinding

// Custom ConfirmDialog class extending Dialog for displaying a confirmation dialog
class ConfirmDialog(
    context: Context // Context object passed during initialization
) : Dialog(context) {
    private lateinit var binding: ConfirmDialogBinding // View binding for the dialog layout
    private var mListener: OnActionListener? = null // Interface for handling user actions

    // Overriding onCreate method for dialog initialization
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflating the dialog layout using view binding
        binding = ConfirmDialogBinding.inflate(layoutInflater)

        // Requesting no title for the dialog
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        // Setting the content view with the inflated layout
        setContentView(binding.root)

        // Calculating the width of the dialog to cover 90% of the screen width
        val width = context.resources.displayMetrics.widthPixels * 0.90

        // Setting the width of the dialog
        binding.root.layoutParams.width = width.toInt()

        // Setting the background of the dialog window to transparent
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Setting click listeners for yes and no buttons
        binding.yes.setOnClickListener {
            mListener?.onOK() // Calling onOK method of the listener
        }
        binding.no.setOnClickListener {
            mListener?.onCancel() // Calling onCancel method of the listener
        }
    }

    // Interface for handling user actions
    interface OnActionListener {
        fun onOK() // Method for handling OK action
        fun onCancel() // Method for handling Cancel action
    }
