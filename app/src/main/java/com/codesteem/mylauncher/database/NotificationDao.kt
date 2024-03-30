@Dao
interface NotificationDao {

    /**
     * Queries the notification_table to retrieve a notification entity with the specified notificationId.
     *
     * @param notificationId The unique identifier of the notification entity to retrieve.
     * @return The notification entity with the specified notificationId, or null if no such entity exists.
     */
    @Query("SELECT * FROM notification_table WHERE id = :notificationId")
    suspend fun getNotificationById(notificationId: String): NotificationEntity?

    /**
     * Deletes a notification entity with the specified notificationId from the notification_table.
    
