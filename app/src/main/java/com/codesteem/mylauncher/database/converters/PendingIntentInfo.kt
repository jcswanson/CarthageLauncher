/**
 * The `PendingIntentInfo` data class holds information about a PendingIntent, which is a token for the system to perform a specific action on your behalf.
 * This class contains four properties:
 *   - `requestCode`: An integer that uniquely identifies the PendingIntent. This can be used to update or cancel the PendingIntent.
 *   - `intent`: The Intent that describes the action to be performed.
 *   - `flags`: Additional flags to control how the Intent is executed.
 *   - `uid`: The UID of the app that created the PendingIntent. This can be used to ensure that only the creating app can update or cancel the PendingIntent.
 *
 * This data class can be used to store and retrieve information about PendingIntents, making it easier to manage them within your app.
 */
data class PendingIntentInfo(
    val requestCode: Int, // Unique identifier for the PendingIntent
    val intent: Intent, // Intent to be executed when the PendingIntent is triggered
    val flags: Int, // Additional flags for the Intent
    val uid: Int // UID of the app that created the PendingIntent
)

/**
 * Returns a string representation of the [PendingIntentInfo] instance.
 */
fun PendingIntentInfo.toString(): String {
    return "PendingIntentInfo(requestCode=$requestCode, intent=${intent.toShortString()}, flags=$flags, uid=$uid)"
}

/**
 * Returns a short string representation of the [Intent] instance.
 */
fun Intent.toShortString(): String {
    val sb = StringBuilder()
    sb.append(action)
    sb.append(": ")
    sb.append(data)
    sb.append(": ")
    sb.append(component)
    return sb.toString()
}
