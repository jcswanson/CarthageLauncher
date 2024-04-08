@Dao
interface NotificationDao {

    /**
     * Gets the [NotificationEntity] object with the given [notificationId] from the database.
     *
     * @param notificationId The unique identifier of the notification to retrieve.
     * @return The [NotificationEntity] object with the given [notificationId], or null if no such object exists.
     */
    @Query("SELECT * FROM notification_table WHERE id = :notificationId")
    suspend fun getNotificationById(notificationId: String): NotificationEntity?

    /**
     * Deletes the [NotificationEntity] object with the given [notificationId] from the database.
     *
     * @param notificationId The unique identifier of the notification to delete.
     */
    @Query("DELETE FROM notification_table WHERE id = :notificationId")
    suspend fun deleteNotificationById(notificationId: String)

    /**
     * Inserts the given [notification] into the database, replacing any existing notification with the same id.
     *
     * @param notification The [NotificationEntity] object to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)

    /**
     * Gets a list of all prioritized [NotificationEntity] objects from the database.
     *
     * @return A list of all prioritized [NotificationEntity] objects.
     */
    @Query("SELECT * FROM notification_table WHERE isPrioritized = 1")
    suspend fun getPrioritizedNotifications(): List<NotificationEntity>

    /**
     * Gets a list of all [NotificationEntity] objects from the database, sorted by timestamp in descending order.
     *
     * @return A list of all [NotificationEntity] objects, sorted by timestamp in descending order.
     */
    @Query("SELECT * FROM notification_table ORDER BY timestamp DESC")
    suspend fun getAllNotifications(): List<NotificationEntity>

    /**
     *
