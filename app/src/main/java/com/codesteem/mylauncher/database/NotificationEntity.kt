package com.codesteem.mylauncher.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.codesteem.mylauncher.database.converters.StringListConverter
import com.codesteem.mylauncher.models.AppInfo

// NotificationEntity: A data class representing a notification item in the local database.
@Entity(tableName = "notification_table") // Entity annotation indicates that this class is a table in the database.
@TypeConverters(StringListConverter::class) // TypeConverters allow Room to convert a List<String> to a single value in the database.
data class NotificationEntity(

    // id: A unique identifier for the notification.
    @PrimaryKey
    val id: String,

    // title: The title of the notification.
    val title: String?,

    // text: The text content of the notification.
    val text: String?,

    // timeStamp: The time the notification was received.
    val timeStamp: Long?,

    // appInfo: Additional information about the app associated with the notification.
    val appInfo: AppInfo?,

    // actions: A list of actions associated with the notification.
    val actions: List<String>,

    // isPrioritized: A flag indicating whether the notification is prioritized.
    var isPrioritized: Boolean = false,

    // isExpanded: A flag indicating whether the notification is expanded.
    var isExpanded: Boolean = false
)
