package com.finora.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.finora.domain.model.NotificationType
import com.finora.domain.model.NotificationPriority
import java.util.Date

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val message: String,
    val type: String,  // NotificationType.name
    val priority: String,  // NotificationPriority.name
    val triggerDate: Date,
    val isRead: Boolean = false,
    val actionDataJson: String? = null,  // JSON string do Map
    val createdAt: Date = Date()
)

@Entity(tableName = "notification_config")
data class NotificationConfigEntity(
    @PrimaryKey
    val id: String = "default",
    val budgetAlertsEnabled: Boolean = true,
    val budgetAlertThreshold: Double = 0.8,
    val dailySummaryEnabled: Boolean = false,
    val dailySummaryTime: String = "20:00",
    val weeklySummaryEnabled: Boolean = true,
    val weeklySummaryDay: Int = 1,
    val weeklySummaryTime: String = "09:00",
    val monthlySummaryEnabled: Boolean = true,
    val monthlySummaryDay: Int = 1,
    val monthlySummaryTime: String = "10:00",
    val insightAlertsEnabled: Boolean = true,
    val spendingSpikeSensitivity: Double = 1.5,
    val reminderEnabled: Boolean = false,
    val reminderFrequencyDays: Int = 3,
    val updatedAt: Date = Date()
)

@Entity(tableName = "notification_schedules")
data class NotificationScheduleEntity(
    @PrimaryKey
    val id: String,
    val type: String,  // NotificationType.name
    val scheduledDate: Date,
    val isRecurring: Boolean = false,
    val recurringPattern: String? = null,  // RecurringPattern.name
    val createdAt: Date = Date()
)
