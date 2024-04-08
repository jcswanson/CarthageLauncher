package com.codesteem.mylauncher.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

/**
 * Copies the given text to the clipboard service of the system.
 * 
 * @param text The text to copy to the clipboard.
 * @param context The context of the application.
 */
fun copyToClipboard(text: String, context: Context) {
    // Get the clipboard manager service from the system
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    // Create a new ClipData with a plain text MIME type and the text to copy
    val clipData = ClipData.newPlainText("text label", text)

    // Set the ClipData to the clipboard
    clipboardManager.setPrimaryClip(clipData)

    // Optionally, you can show a toast or provide feedback to the user
    // For example:
    Toast.makeText(context, "Search result has been copied to clipboard", Toast.LENGTH_SHORT).show()
}

/**
 * Hides the keyboard associated with the given view.
 * 
 * @param view The view associated with the keyboard to hide.
 */
fun hideKeyboard(view: View) {
    // Get the input method manager service from the system
    val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    // Hide the soft input window from the view
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}
