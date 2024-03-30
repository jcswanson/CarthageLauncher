package com.codesteem.mylauncher.bug_report

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.codesteem.mylauncher.databinding.ConfirmDialogBinding

class ConfirmDialog(
    context: Context,
    private val mListener: OnActionListener? = null
) : Dialog(context) {
    private lateinit var binding: ConfirmDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ConfirmDialogBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
        val width = context.resources.displayMetrics.widthPixels * 0.90
        binding.root.layoutParams.width = width.toInt()
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.yes.setOnClickListener {
            mListener?.onOK()
            dismiss()
        }
        binding.no.setOnClickListener {
            mListener?.onCancel()
            dismiss()
        }
    }

    interface OnActionListener {
        abstract fun onOK()
        abstract fun onCancel()
    }
}
