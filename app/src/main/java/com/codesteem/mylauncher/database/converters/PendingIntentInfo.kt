// The `PendingIntentInfo` data class holds information about a PendingIntent, which is a token for the system to perform a specific action on your behalf.
// This class contains three properties:
//   - `requestCode`: An integer that uniquely identifies the PendingIntent. This can be used to update or cancel the PendingIntent.
//   - `intent`: The Intent that describes the action to be performed.
//   - `flags`: Additional flags to control how the Intent is executed.
//
// This data class can be used to store and retrieve information about PendingIntents, making it easier to manage them within your app.
data class PendingIntentInfo(
    val requestCode: Int, // Unique identifier for the PendingIntent
    val intent: Intent, // Intent to be executed when the PendingIntent is triggered
    val flags: Int // Additional flags for the Intent
    // Add other necessary information
)
