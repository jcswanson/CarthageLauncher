/**
 * Data class representing the details of a notification action.
 *
 * @property title The title of the notification action. This should be a short, descriptive
 * string that clearly communicates the purpose of the action.
 * @property actionIntent The pending intent that will be triggered when the user selects this
 * notification action. This should be an instance of [PendingIntent] that has been properly
 * set up with the appropriate action and request code.
 */
data class NotificationActionDetails(
    val title: CharSequence, // Non-null title
    val actionIntent: PendingIntent? // Nullable action intent
) {

    /**
     * Returns true if both title and actionIntent are non-null.
     */
    fun isValid(): Boolean {
        return title != null && actionIntent != null
    }
}
