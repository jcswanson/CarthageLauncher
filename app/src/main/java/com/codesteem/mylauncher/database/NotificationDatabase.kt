package com.codesteem.mylauncher.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.codesteem.mylauncher.database.converters.StringListConverter


/**
 * A database class that manages the [NotificationEntity] objects.
 * This class is responsible for creating, updating, and deleting [NotificationEntity] objects.
 */
@Database(entities = [NotificationEntity::class], version = 1, exportSchema = false)
@TypeConverters(StatusBarNotificationConverter::class, AppInfoConverter::class, StringListConverter::class)
abstract class NotificationDatabase : RoomDatabase() {

    /**
     * Returns the [NotificationDao] object that provides methods for interacting with [NotificationEntity] objects.
     */
    abstract fun notificationDao(): NotificationDao

    /**
     * A companion object that contains a nullable [NotificationDatabase] instance.
     * This object also provides a method to get the [NotificationDatabase] instance.
     */
    companion object {
        @Volatile
        private var INSTANCE: NotificationDatabase? = null

        /**
         * Returns the [NotificationDatabase] instance for the given [context].
         * If the instance is not created, it creates a new instance using the [Room.databaseBuilder] method.
         */
        fun getDatabase(context: Context): NotificationDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotificationDatabase::class.java,
                    "notification_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
