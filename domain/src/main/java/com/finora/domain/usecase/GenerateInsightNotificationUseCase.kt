package com.finora.domain.usecase

import com.finora.core.AppError
import com.finora.core.DispatcherProvider
import com.finora.core.Result
import com.finora.domain.model.*
import com.finora.domain.repository.NotificationRepository
import java.util.*
import javax.inject.Inject

/**
 * UseCase para gerar notificações baseadas em insights de gastos
 */
class GenerateInsightNotificationUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val getMonthlyInsightsUseCase: GetMonthlyInsightsUseCase,
    dispatcherProvider: DispatcherProvider
) : UseCase<Unit, List<FinoraNotification>>(dispatcherProvider) {
    
    override suspend fun execute(parameters: Unit): Result<List<FinoraNotification>, AppError> {
        val config = notificationRepository.getNotificationConfig()
        
        // Se alertas de insight estão desabilitados, retorna vazio
        if (!config.insightAlertsEnabled) {
            return Result.Success(emptyList())
        }
        
        val notifications = mutableListOf<FinoraNotification>()
        
        // Obtém insights do mês atual
        val insightResult = getMonthlyInsightsUseCase(GetMonthlyInsightsUseCase.Params(Date()))
        val insight = when (insightResult) {
            is Result.Success -> insightResult.data
            is Result.Failure -> return Result.Success(emptyList())
        }
        
        // Verifica pico de gastos comparado com mês anterior
        insight.comparisonWithLastMonth?.let { comparison ->
            if (comparison.trend == SpendingTrend.INCREASING) {
                val increasePercentage = comparison.differencePercentage
                
                // Se o aumento for significativo (acima do threshold configurado)
                if (increasePercentage >= (config.spendingSpikeSensitivity - 1) * 100) {
                    val notification = FinoraNotification(
                        id = UUID.randomUUID().toString(),
                        title = "📊 Aumento de Gastos Detectado",
                        message = "Seus gastos aumentaram ${increasePercentage.toInt()}% " +
                                "em relação ao mês passado. Vale a pena revisar!",
                        type = NotificationType.SPENDING_SPIKE,
                        priority = NotificationPriority.MEDIUM,
                        triggerDate = Date(),
                        isRead = false,
                        actionData = mapOf(
                            "screen" to "insights_dashboard",
                            "increasePercentage" to increasePercentage.toString()
                        )
                    )
                    
                    notifications.add(notification)
                    notificationRepository.saveNotification(notification)
                }
            }
        }
        
        // Alerta sobre categoria com maior gasto
        val topCategory = insight.categoryBreakdown.firstOrNull()
        if (topCategory != null && topCategory.percentage >= 40.0) {
            // Se uma categoria representa mais de 40% dos gastos
            val notification = FinoraNotification(
                id = UUID.randomUUID().toString(),
                title = "💡 Concentração de Gastos",
                message = "${topCategory.category.displayName} representa ${topCategory.percentage.toInt()}% " +
                        "dos seus gastos este mês.",
                type = NotificationType.CATEGORY_WARNING,
                priority = NotificationPriority.LOW,
                triggerDate = Date(),
                isRead = false,
                actionData = mapOf(
                    "category" to topCategory.category.name,
                    "percentage" to topCategory.percentage.toString()
                )
            )
            
            notifications.add(notification)
            notificationRepository.saveNotification(notification)
        }
        
        // Alerta sobre dia mais caro
        insight.mostExpensiveDay?.let { day ->
            val daySpending = insight.mostExpensiveDayAmount
            val averageDaily = insight.averageDailySpending
            
            // Se o dia mais caro foi mais de 3x a média diária
            if (daySpending >= averageDaily * 3) {
                val notification = FinoraNotification(
                    id = UUID.randomUUID().toString(),
                    title = "💰 Dia de Alto Gasto Detectado",
                    message = "No dia ${java.text.SimpleDateFormat("dd/MM", Locale.getDefault()).format(day)} " +
                            "você gastou 3x mais que sua média diária.",
                    type = NotificationType.INSIGHT_ALERT,
                    priority = NotificationPriority.LOW,
                    triggerDate = Date(),
                    isRead = false,
                    actionData = mapOf(
                        "date" to day.time.toString(),
                        "amount" to daySpending.toString()
                    )
                )
                
                notifications.add(notification)
                notificationRepository.saveNotification(notification)
            }
        }
        
        return Result.Success(notifications)
    }
}
