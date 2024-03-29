package com.codesteem.mylauncher.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.codesteem.mylauncher.database.converters.StringListConverter
import com.codesteem.mylauncher.models.AppInfo

@Entity(tableName = "notification_table")
@TypeConverters(StringListConverter::class)
data class NotificationEntity(
    @PrimaryKey
    val id: String,
    val title: String?,
    val text: String?,
    val timeStamp: Long?,
    val appInfo: AppInfo?,
    val actions: List<String>,
    var isPrioritized: Boolean = false,
    var isExpanded: Boolean = false
)
