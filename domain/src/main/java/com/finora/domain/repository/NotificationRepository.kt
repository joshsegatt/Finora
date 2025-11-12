package com.finora.domain.repository

import com.finora.domain.model.FinoraNotification
import com.finora.domain.model.NotificationConfig
import com.finora.domain.model.NotificationSchedule
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    
    /**
     * Obtém a configuração de notificações do usuário
     */
    suspend fun getNotificationConfig(): NotificationConfig
    
    /**
     * Salva a configuração de notificações
     */
    suspend fun saveNotificationConfig(config: NotificationConfig)
    
    /**
     * Obtém todas as notificações
     */
    fun getAllNotifications(): Flow<List<FinoraNotification>>
    
    /**
     * Obtém notificações não lidas
     */
    fun getUnreadNotifications(): Flow<List<FinoraNotification>>
    
    /**
     * Salva uma nova notificação
     */
    suspend fun saveNotification(notification: FinoraNotification)
    
    /**
     * Marca notificação como lida
     */
    suspend fun markAsRead(notificationId: String)
    
    /**
     * Marca todas as notificações como lidas
     */
    suspend fun markAllAsRead()
    
    /**
     * Deleta uma notificação
     */
    suspend fun deleteNotification(notificationId: String)
    
    /**
     * Deleta todas as notificações
     */
    suspend fun deleteAllNotifications()
    
    /**
     * Obtém agendamentos de notificações
     */
    fun getScheduledNotifications(): Flow<List<NotificationSchedule>>
    
    /**
     * Salva agendamento de notificação
     */
    suspend fun saveSchedule(schedule: NotificationSchedule)
    
    /**
     * Cancela agendamento de notificação
     */
    suspend fun cancelSchedule(scheduleId: String)
}
