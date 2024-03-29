package com.codesteem.mylauncher.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NotificationDao {

    @Query("SELECT * FROM notification_table WHERE id = :notificationId")
    suspend fun getNotificationById(notificationId: String): NotificationEntity?

    @Query("DELETE FROM notification_table WHERE id = :notificationId")
    suspend fun deleteNotificationById(notificationId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)

    @Query("SELECT * FROM notification_table WHERE isPrioritized = 1")
    suspend fun getPrioritizedNotifications(): List<NotificationEntity>

    @Query("SELECT * FROM notification_table ORDER BY timestamp DESC")
    suspend fun getAllNotifications(): List<NotificationEntity>

    @Query("DELETE FROM notification_table")
    suspend fun deleteAllNotifications()
}
