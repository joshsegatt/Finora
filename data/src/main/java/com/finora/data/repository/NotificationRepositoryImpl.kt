package com.finora.data.repository

import com.finora.data.local.dao.NotificationDao
import com.finora.data.local.entity.NotificationConfigEntity
import com.finora.data.local.entity.NotificationEntity
import com.finora.data.local.entity.NotificationScheduleEntity
import com.finora.domain.model.*
import com.finora.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONObject
import java.util.*
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val notificationDao: NotificationDao
) : NotificationRepository {
    
    override suspend fun getNotificationConfig(): NotificationConfig {
        val entity = notificationDao.getConfig() ?: NotificationConfigEntity()
        return entity.toDomain()
    }
    
    override suspend fun saveNotificationConfig(config: NotificationConfig) {
        notificationDao.insertConfig(config.toEntity())
    }
    
    override fun getAllNotifications(): Flow<List<FinoraNotification>> {
        return notificationDao.getAllNotifications().map { list ->
            list.map { it.toDomain() }
        }
    }
    
    override fun getUnreadNotifications(): Flow<List<FinoraNotification>> {
        return notificationDao.getUnreadNotifications().map { list ->
            list.map { it.toDomain() }
        }
    }
    
    override suspend fun saveNotification(notification: FinoraNotification) {
        notificationDao.insertNotification(notification.toEntity())
    }
    
    override suspend fun markAsRead(notificationId: String) {
        notificationDao.markAsRead(notificationId)
    }
    
    override suspend fun markAllAsRead() {
        notificationDao.markAllAsRead()
    }
    
    override suspend fun deleteNotification(notificationId: String) {
        notificationDao.deleteNotification(notificationId)
    }
    
    override suspend fun deleteAllNotifications() {
        notificationDao.deleteAllNotifications()
    }
    
    override fun getScheduledNotifications(): Flow<List<NotificationSchedule>> {
        return notificationDao.getAllSchedules().map { list ->
            list.map { it.toDomain() }
        }
    }
    
    override suspend fun saveSchedule(schedule: NotificationSchedule) {
        notificationDao.insertSchedule(schedule.toEntity())
    }
    
    override suspend fun cancelSchedule(scheduleId: String) {
        notificationDao.deleteSchedule(scheduleId)
    }
}

// ===== Mappers Entity -> Domain =====

private fun NotificationEntity.toDomain(): FinoraNotification {
    val actionData = actionDataJson?.let { json ->
        try {
            val jsonObject = JSONObject(json)
            val map = mutableMapOf<String, String>()
            jsonObject.keys().forEach { key ->
                map[key] = jsonObject.getString(key)
            }
            map
        } catch (e: Exception) {
            null
        }
    }
    
    return FinoraNotification(
        id = id,
        title = title,
        message = message,
        type = NotificationType.valueOf(type),
        priority = NotificationPriority.valueOf(priority),
        triggerDate = triggerDate,
        isRead = isRead,
        actionData = actionData
    )
}

private fun NotificationConfigEntity.toDomain(): NotificationConfig {
    return NotificationConfig(
        id = id,
        budgetAlertsEnabled = budgetAlertsEnabled,
        budgetAlertThreshold = budgetAlertThreshold,
        dailySummaryEnabled = dailySummaryEnabled,
        dailySummaryTime = dailySummaryTime,
        weeklySummaryEnabled = weeklySummaryEnabled,
        weeklySummaryDay = weeklySummaryDay,
        weeklySummaryTime = weeklySummaryTime,
        monthlySummaryEnabled = monthlySummaryEnabled,
        monthlySummaryDay = monthlySummaryDay,
        monthlySummaryTime = monthlySummaryTime,
        insightAlertsEnabled = insightAlertsEnabled,
        spendingSpikeSensitivity = spendingSpikeSensitivity,
        reminderEnabled = reminderEnabled,
        reminderFrequencyDays = reminderFrequencyDays,
        updatedAt = updatedAt
    )
}

private fun NotificationScheduleEntity.toDomain(): NotificationSchedule {
    return NotificationSchedule(
        id = id,
        type = NotificationType.valueOf(type),
        scheduledDate = scheduledDate,
        isRecurring = isRecurring,
        recurringPattern = recurringPattern?.let { RecurringPattern.valueOf(it) }
    )
}

// ===== Mappers Domain -> Entity =====

private fun FinoraNotification.toEntity(): NotificationEntity {
    val actionDataJson = actionData?.let { map ->
        JSONObject(map).toString()
    }
    
    return NotificationEntity(
        id = id,
        title = title,
        message = message,
        type = type.name,
        priority = priority.name,
        triggerDate = triggerDate,
        isRead = isRead,
        actionDataJson = actionDataJson,
        createdAt = Date()
    )
}

private fun NotificationConfig.toEntity(): NotificationConfigEntity {
    return NotificationConfigEntity(
        id = id,
        budgetAlertsEnabled = budgetAlertsEnabled,
        budgetAlertThreshold = budgetAlertThreshold,
        dailySummaryEnabled = dailySummaryEnabled,
        dailySummaryTime = dailySummaryTime,
        weeklySummaryEnabled = weeklySummaryEnabled,
        weeklySummaryDay = weeklySummaryDay,
        weeklySummaryTime = weeklySummaryTime,
        monthlySummaryEnabled = monthlySummaryEnabled,
        monthlySummaryDay = monthlySummaryDay,
        monthlySummaryTime = monthlySummaryTime,
        insightAlertsEnabled = insightAlertsEnabled,
        spendingSpikeSensitivity = spendingSpikeSensitivity,
        reminderEnabled = reminderEnabled,
        reminderFrequencyDays = reminderFrequencyDays,
        updatedAt = updatedAt
    )
}

private fun NotificationSchedule.toEntity(): NotificationScheduleEntity {
    return NotificationScheduleEntity(
        id = id,
        type = type.name,
        scheduledDate = scheduledDate,
        isRecurring = isRecurring,
        recurringPattern = recurringPattern?.name,
        createdAt = Date()
    )
}
