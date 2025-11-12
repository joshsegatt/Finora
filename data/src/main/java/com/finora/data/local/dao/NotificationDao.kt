package com.finora.data.local.dao

import androidx.room.*
import com.finora.data.local.entity.NotificationConfigEntity
import com.finora.data.local.entity.NotificationEntity
import com.finora.data.local.entity.NotificationScheduleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    
    // ===== NotificationConfig =====
    
    @Query("SELECT * FROM notification_config WHERE id = 'default' LIMIT 1")
    suspend fun getConfig(): NotificationConfigEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConfig(config: NotificationConfigEntity)
    
    
    // ===== Notifications =====
    
    @Query("SELECT * FROM notifications ORDER BY triggerDate DESC")
    fun getAllNotifications(): Flow<List<NotificationEntity>>
    
    @Query("SELECT * FROM notifications WHERE isRead = 0 ORDER BY triggerDate DESC")
    fun getUnreadNotifications(): Flow<List<NotificationEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)
    
    @Query("UPDATE notifications SET isRead = 1 WHERE id = :notificationId")
    suspend fun markAsRead(notificationId: String)
    
    @Query("UPDATE notifications SET isRead = 1")
    suspend fun markAllAsRead()
    
    @Query("DELETE FROM notifications WHERE id = :notificationId")
    suspend fun deleteNotification(notificationId: String)
    
    @Query("DELETE FROM notifications")
    suspend fun deleteAllNotifications()
    
    
    // ===== Notification Schedules =====
    
    @Query("SELECT * FROM notification_schedules ORDER BY scheduledDate ASC")
    fun getAllSchedules(): Flow<List<NotificationScheduleEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedule(schedule: NotificationScheduleEntity)
    
    @Query("DELETE FROM notification_schedules WHERE id = :scheduleId")
    suspend fun deleteSchedule(scheduleId: String)
}
