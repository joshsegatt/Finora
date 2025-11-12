package com.finora.domain.usecase

import com.finora.core.AppError
import com.finora.core.DispatcherProvider
import com.finora.core.Result
import com.finora.domain.model.MonthlyPrediction
import com.finora.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.first
import java.util.*
import javax.inject.Inject

/**
 * PredictMonthlySpendingUseCase - Previsão baseada em média histórica.
 * 
 * ALGORITMO SIMPLES:
 * - Calcula média dos últimos 3 meses
 * - Confidence baseado em consistência
 * - Recomendação se estiver acima da média
 */
class PredictMonthlySpendingUseCase @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    dispatcherProvider: DispatcherProvider
) : UseCase<Unit, MonthlyPrediction>(dispatcherProvider) {
    
    override suspend fun execute(params: Unit): Result<MonthlyPrediction, AppError> {
        return try {
            val calendar = Calendar.getInstance()
            val monthlyTotals = mutableListOf<Double>()
            
            // Buscar últimos 3 meses
            for (i in 1..3) {
                calendar.add(Calendar.MONTH, -1)
                
                calendar.set(Calendar.DAY_OF_MONTH, 1)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                val startOfMonth = calendar.time
                
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
                calendar.set(Calendar.HOUR_OF_DAY, 23)
                calendar.set(Calendar.MINUTE, 59)
                calendar.set(Calendar.SECOND, 59)
                val endOfMonth = calendar.time
                
                val expenses = expenseRepository.getExpensesByDateRange(startOfMonth, endOfMonth).first()
                val total = expenses.fold(0.0) { acc, expense -> acc + expense.amount }
                monthlyTotals.add(total)
            }
            
            if (monthlyTotals.all { it == 0.0 }) {
                return Result.Success(
                    MonthlyPrediction(
                        predictedAmount = 0.0,
                        confidence = 0.0f,
                        basedOnMonths = 0,
                        recommendation = "Sem dados suficientes para previsão"
                    )
                )
            }
            
            // Calcular média
            val average = monthlyTotals.average()
            
            // Calcular variação (confidence)
            val variance = monthlyTotals.map { (it - average) * (it - average) }.average()
            val stdDev = kotlin.math.sqrt(variance)
            val coefficientOfVariation = if (average > 0) (stdDev / average).toFloat() else 0f
            
            // Confidence: quanto menor a variação, maior a confiança
            val confidence = (1 - coefficientOfVariation.coerceIn(0f, 1f)) * 100
            
            // Recomendação
            val currentMonthStart = Calendar.getInstance().apply {
                set(Calendar.DAY_OF_MONTH, 1)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
            }.time
            
            val currentMonthExpenses = expenseRepository.getExpensesByDateRange(
                currentMonthStart,
                Date()
            ).first()
            
            val currentMonthTotal = currentMonthExpenses.fold(0.0) { acc, expense -> acc + expense.amount }
            
            val recommendation = when {
                currentMonthTotal > average * 1.2 -> "⚠️ Gastos 20% acima da média. Considere revisar despesas."
                currentMonthTotal > average -> "📊 Gastos ligeiramente acima da média."
                else -> "✅ Gastos dentro da média esperada."
            }
            
            Result.Success(
                MonthlyPrediction(
                    predictedAmount = average,
                    confidence = confidence,
                    basedOnMonths = monthlyTotals.count { it > 0 },
                    recommendation = recommendation
                )
            )
            
        } catch (e: Exception) {
            Result.Failure(AppError.fromThrowable(e))
        }
    }
}
