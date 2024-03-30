package com.codesteem.mylauncher.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.codesteem.mylauncher.database.converters.StringListConverter


/**
 * A database class that manages the [NotificationEntity] objects in the app.
 * This class is responsible for creating, reading, updating, and deleting [NotificationEntity] objects.
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
     * This object also provides a method for getting the [NotificationDatabase] instance.
     */
    companion object {
        @Volatile
        private var INSTANCE: NotificationDatabase? = null

        /**
         * Returns the [NotificationDatabase] instance for the given [context].
         * If the instance is not created yet, it will be created and cached.
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
