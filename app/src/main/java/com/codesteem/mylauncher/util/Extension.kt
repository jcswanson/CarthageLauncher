import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

/**
 * Copies the given text to the clipboard service of the system.
 *
 * @param text The text to copy to the clipboard.
 * @param context The context of the application.
 */
fun String.copyToClipboard(context: Context?) {
    if (context == null) {
        throw IllegalArgumentException("Context cannot be null")
    }

    // Get the clipboard manager service from the system
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    // Create a new ClipData with a plain text MIME type and the text to copy
    val clipData = ClipData.newPlainText("text label", this)

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
fun View.hideKeyboard() {
    // Get the input method manager service from the system
    val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    // Check if the device is running Android 6.0 (API level 23) or higher
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        // Use the new hideSoftInputFromWindow() method with the FLAG_HIDE_SOFT_INPUT flag
        imm.hideSoftInputFromWindow(this.window
