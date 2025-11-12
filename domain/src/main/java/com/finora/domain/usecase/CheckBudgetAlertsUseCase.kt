package com.finora.domain.usecase

import com.finora.core.AppError
import com.finora.core.DispatcherProvider
import com.finora.core.Result
import com.finora.domain.model.*
import com.finora.domain.repository.BudgetRepository
import com.finora.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.first
import java.util.*
import javax.inject.Inject

/**
 * UseCase para verificar alertas de orçamentos e gerar notificações
 */
class CheckBudgetAlertsUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository,
    private val notificationRepository: NotificationRepository,
    private val calculateBudgetProgressUseCase: CalculateBudgetProgressUseCase,
    dispatcherProvider: DispatcherProvider
) : UseCase<Unit, AlertCheckResult>(dispatcherProvider) {
    
    override suspend fun execute(parameters: Unit): Result<AlertCheckResult, AppError> {
        val config = notificationRepository.getNotificationConfig()
        
        // Se alertas de orçamento estão desabilitados, retorna vazio
        if (!config.budgetAlertsEnabled) {
            return Result.Success(
                AlertCheckResult(
                    budgetAlerts = emptyList(),
                    insightAlerts = emptyList(),
                    hasUrgentAlerts = false
                )
            )
        }
        
        // Busca todos os orçamentos ativos
        val budgets = budgetRepository.getAllActiveBudgets().first()
        
        val alerts = mutableListOf<BudgetAlert>()
        
        for (budget in budgets) {
            val progressResult = calculateBudgetProgressUseCase(
                CalculateBudgetProgressUseCase.Params(budget.id)
            )
            val progress = when (progressResult) {
                is Result.Success -> progressResult.data
                is Result.Failure -> continue
            }
            
            // Verifica se deve gerar alerta
            when {
                // Orçamento excedido - alerta URGENTE
                progress.status == BudgetStatus.EXCEEDED -> {
                    alerts.add(
                        BudgetAlert(
                            budget = budget,
                            currentProgress = progress,
                            message = "Orçamento de ${budget.category.displayName} excedido! " +
                                    "Você gastou ${progress.percentage.toInt()}% do limite.",
                            severity = NotificationPriority.URGENT
                        )
                    )
                    
                    // Cria notificação
                    createNotification(
                        type = NotificationType.BUDGET_EXCEEDED,
                        title = "⚠️ Orçamento Excedido",
                        message = "${budget.category.displayName}: você ultrapassou o limite em ${(progress.percentage - 100).toInt()}%",
                        priority = NotificationPriority.URGENT,
                        actionData = mapOf(
                            "budgetId" to budget.id.toString(),
                            "category" to budget.category.name
                        )
                    )
                }
                
                // Orçamento no limite de aviso - alerta HIGH
                progress.status == BudgetStatus.WARNING && 
                progress.percentage >= (config.budgetAlertThreshold * 100) -> {
                    alerts.add(
                        BudgetAlert(
                            budget = budget,
                            currentProgress = progress,
                            message = "Atenção! Você já usou ${progress.percentage.toInt()}% " +
                                    "do orçamento de ${budget.category.displayName}.",
                            severity = NotificationPriority.HIGH
                        )
                    )
                    
                    // Cria notificação
                    createNotification(
                        type = NotificationType.BUDGET_ALERT,
                        title = "⚠️ Atenção ao Orçamento",
                        message = "${budget.category.displayName}: ${progress.percentage.toInt()}% usado",
                        priority = NotificationPriority.HIGH,
                        actionData = mapOf(
                            "budgetId" to budget.id.toString(),
                            "category" to budget.category.name
                        )
                    )
                }
            }
        }
        
        val hasUrgent = alerts.any { it.severity == NotificationPriority.URGENT }
        
        return Result.Success(
            AlertCheckResult(
                budgetAlerts = alerts,
                insightAlerts = emptyList(),  // Será preenchido pelo GenerateInsightNotificationUseCase
                hasUrgentAlerts = hasUrgent
            )
        )
    }
    
    private suspend fun createNotification(
        type: NotificationType,
        title: String,
        message: String,
        priority: NotificationPriority,
        actionData: Map<String, String>? = null
    ) {
        val notification = FinoraNotification(
            id = UUID.randomUUID().toString(),
            title = title,
            message = message,
            type = type,
            priority = priority,
            triggerDate = Date(),
            isRead = false,
            actionData = actionData
        )
        
        notificationRepository.saveNotification(notification)
    }
}
