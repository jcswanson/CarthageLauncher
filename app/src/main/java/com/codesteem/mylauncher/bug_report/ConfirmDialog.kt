package com.codesteem.mylauncher.bug_report

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.codesteem.mylauncher.databinding.ConfirmDialogBinding
import com.codesteem.mylauncher.databinding.LoadingDialogBinding

class ConfirmDialog(
    context: Context
): Dialog(context) {
    private lateinit var binding: ConfirmDialogBinding
    private var mListener: OnActionListener? = null

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
        }
        binding.no.setOnClickListener {
            mListener?.onCancel()
        }
    }

    interface OnActionListener {
        fun onOK()
        fun onCancel()
    }

    fun setOnActionListener(listener: OnActionListener?) {
        mListener = listener
    }

}