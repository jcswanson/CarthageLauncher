package com.codesteem.mylauncher.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.codesteem.mylauncher.database.converters.StringListConverter
import com.codesteem.mylauncher.models.AppInfo

/**
 * Data class representing a notification stored in the Room database.
 *
 * @property id The unique identifier for the notification.
 * @property title The title of the notification.
 * @property text The text content of the notification.
 * @property timeStamp The timestamp indicating when the notification was received.
 * @property appInfo Information about the app associated with the notification.
 * @property actions A list of actions that can be performed on the notification.
 * @property isPrioritized A flag indicating whether the notification is prioritized.
 * @property isExpanded A flag indicating whether the notification is expanded.
 */
@Entity(tableName = "notification_table")
@TypeConverters(StringListConverter::class)
data class NotificationEntity(
    @PrimaryKey
    val id: String, // The unique identifier for the notification
    val title: String?, // The title of the notification
    val text: String?, // The text content of the notification
    val timeStamp: Long?, // The timestamp indicating when the notification was received
    val appInfo: AppInfo?, // Information about the app associated with the notification
    val actions: List<String>, // A list of actions that can be performed on the notification
    var isPrioritized: Boolean = false, // A flag indicating whether the notification is prioritized
    var isExpanded: Boolean = false // A flag indicating whether the notification is expanded
)
