package com.codesteem.mylauncher.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.codesteem.mylauncher.database.converters.StringListConverter
import com.codesteem.mylauncher.models.AppInfo

@Entity(tableName = "notification_table")
@TypeConverters(StringListConverter::class)
data class NotificationEntity(
    @PrimaryKey val id: String,
    val title: String?,
    val text: String?,
    val timeStamp: Long?,
    val appInfo: AppInfo?,
    @TypeConverters(StringListConverter::class)
    val actions: List<String>,
    var isPrioritized: Boolean = false,
    var isExpanded: Boolean = false
)

// AppInfoEntity: A data class representing app info in the local database.
@Entity(tableName = "app_info_table")
data class AppInfoEntity(
    @PrimaryKey val packageName: String,
    val label: String,
    val icon: String
)

// Converters for converting List<String> to String and vice versa.
class StringListConverter {
    @TypeConverter
    fun fromString(value: String): List<String> {
        return value.split(",")
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return list.joinToString(",")
    }
}
