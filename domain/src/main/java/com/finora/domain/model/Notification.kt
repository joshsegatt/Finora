package com.finora.domain.model

import java.util.Date

/**
 * Representa uma notificação no sistema Finora
 */
data class FinoraNotification(
    val id: String,
    val title: String,
    val message: String,
    val type: NotificationType,
    val priority: NotificationPriority,
    val triggerDate: Date,
    val isRead: Boolean = false,
    val actionData: Map<String, String>? = null
)

/**
 * Tipos de notificação no app
 */
enum class NotificationType {
    BUDGET_ALERT,          // Alerta de orçamento próximo/excedido
    BUDGET_EXCEEDED,       // Orçamento excedido
    DAILY_SUMMARY,         // Resumo diário de gastos
    WEEKLY_SUMMARY,        // Resumo semanal
    MONTHLY_SUMMARY,       // Resumo mensal
    INSIGHT_ALERT,         // Alerta baseado em insights (padrão anormal)
    SPENDING_SPIKE,        // Detecção de pico de gastos
    CATEGORY_WARNING,      // Aviso sobre categoria específica
    REMINDER              // Lembrete para adicionar despesas
}

/**
 * Prioridade da notificação
 */
enum class NotificationPriority {
    LOW,      // Notificações informativas
    MEDIUM,   // Avisos importantes
    HIGH,     // Alertas críticos
    URGENT    // Requer ação imediata
}

/**
 * Configuração de notificações do usuário
 */
data class NotificationConfig(
    val id: String = "default",
    val budgetAlertsEnabled: Boolean = true,
    val budgetAlertThreshold: Double = 0.8,  // 80% do limite
    val dailySummaryEnabled: Boolean = false,
    val dailySummaryTime: String = "20:00",   // HH:mm
    val weeklySummaryEnabled: Boolean = true,
    val weeklySummaryDay: Int = 1,            // 1=Segunda-feira
    val weeklySummaryTime: String = "09:00",
    val monthlySummaryEnabled: Boolean = true,
    val monthlySummaryDay: Int = 1,           // Dia 1 do mês
    val monthlySummaryTime: String = "10:00",
    val insightAlertsEnabled: Boolean = true,
    val spendingSpikeSensitivity: Double = 1.5,  // 150% da média = spike
    val reminderEnabled: Boolean = false,
    val reminderFrequencyDays: Int = 3,
    val updatedAt: Date = Date()
)

/**
 * Representação de um alerta de orçamento
 */
data class BudgetAlert(
    val budget: Budget,
    val currentProgress: BudgetProgress,
    val message: String,
    val severity: NotificationPriority
)

/**
 * Dados para agendamento de notificação
 */
data class NotificationSchedule(
    val id: String,
    val type: NotificationType,
    val scheduledDate: Date,
    val isRecurring: Boolean = false,
    val recurringPattern: RecurringPattern? = null
)

/**
 * Padrão de recorrência para notificações
 */
enum class RecurringPattern {
    DAILY,
    WEEKLY,
    MONTHLY
}

/**
 * Resultado da verificação de alertas
 */
data class AlertCheckResult(
    val budgetAlerts: List<BudgetAlert>,
    val insightAlerts: List<FinoraNotification>,
    val hasUrgentAlerts: Boolean
)
