package com.codesteem.mylauncher.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

fun copyToClipboard(text: String, context: Context) {
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    // Create a new ClipData with a plain text MIME type and the text to copy
    val clipData = ClipData.newPlainText("text label", text)

    // Set the ClipData to the clipboard
    clipboardManager.setPrimaryClip(clipData)

    // Optionally, you can show a toast or provide feedback to the user
    // For example:
    Toast.makeText(context, "Search result has been copied to clipboard", Toast.LENGTH_SHORT).show()
}

fun hideKeyboard(view: View) {
    val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}