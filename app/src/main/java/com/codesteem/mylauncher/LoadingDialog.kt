package com.codesteem.mylauncher

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.codesteem.mylauncher.databinding.LoadingDialogBinding

class LoadingDialog(
    context: Context,
    private val title: String?
): Dialog(context) {
    private lateinit var binding: LoadingDialogBinding
    private var mListener: OnActionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoadingDialogBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
        val width = context.resources.displayMetrics.widthPixels * 0.90
        binding.root.layoutParams.width = width.toInt()
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        if (title != null) {
            binding.title.text = title
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