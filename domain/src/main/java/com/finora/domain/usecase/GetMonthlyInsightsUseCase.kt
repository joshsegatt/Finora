package com.finora.domain.usecase

import com.finora.core.AppError
import com.finora.core.DispatcherProvider
import com.finora.core.Result
import com.finora.domain.model.*
import com.finora.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.first
import java.util.*
import javax.inject.Inject
import kotlin.math.abs

/**
 * GetMonthlyInsightsUseCase - Análise completa do mês.
 * 
 * FUNCIONALIDADES:
 * - Total gasto no mês
 * - Breakdown por categoria
 * - Categoria mais cara
 * - Média diária
 * - Comparação com mês anterior
 * - Dia mais caro
 */
class GetMonthlyInsightsUseCase @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    dispatcherProvider: DispatcherProvider
) : UseCase<GetMonthlyInsightsUseCase.Params, MonthlyInsight>(dispatcherProvider) {
    
    data class Params(val month: Date)
    
    override suspend fun execute(params: Params): Result<MonthlyInsight, AppError> {
        return try {
            val calendar = Calendar.getInstance()
            calendar.time = params.month
            
            // Definir início e fim do mês
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
            
            // Buscar despesas do mês
            val expenses = expenseRepository.getExpensesByDateRange(startOfMonth, endOfMonth).first()
            
            if (expenses.isEmpty()) {
                return Result.Success(createEmptyInsight(params.month))
            }
            
            // Calcular total
            val totalSpent = expenses.fold(0.0) { acc, expense -> acc + expense.amount }
            
            // Breakdown por categoria
            val categoryMap = expenses.groupBy { it.category }
            val categoryBreakdown = categoryMap.map { (category, expensesInCategory) ->
                val amount = expensesInCategory.fold(0.0) { acc, expense -> acc + expense.amount }
                val percentage = ((amount / totalSpent) * 100).toFloat()
                CategorySpending(
                    category = category,
                    amount = amount,
                    percentage = percentage,
                    count = expensesInCategory.size
                )
            }.sortedByDescending { it.amount }
            
            // Categoria mais cara
            val topCategory = categoryBreakdown.firstOrNull()?.category ?: ExpenseCategory.OTHER
            
            // Média diária
            val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            val averageDailySpending = totalSpent / daysInMonth
            
            // Dia mais caro
            val expensesByDay = expenses.groupBy { 
                Calendar.getInstance().apply { time = it.date }.get(Calendar.DAY_OF_MONTH)
            }
            val mostExpensiveDay = expensesByDay.maxByOrNull { (_, expensesInDay) ->
                expensesInDay.fold(0.0) { acc, expense -> acc + expense.amount }
            }
            val mostExpensiveDayAmount = mostExpensiveDay?.let { (_, expensesInDay) ->
                expensesInDay.fold(0.0) { acc, expense -> acc + expense.amount }
            } ?: 0.0
            val mostExpensiveDayDate = mostExpensiveDay?.let { (day, _) ->
                Calendar.getInstance().apply {
                    time = params.month
                    set(Calendar.DAY_OF_MONTH, day)
                }.time
            }
            
            // Comparação com mês anterior
            val comparison = calculateComparison(
                currentMonth = params.month,
                currentTotal = totalSpent
            )
            
            val insight = MonthlyInsight(
                month = params.month,
                totalSpent = totalSpent,
                categoryBreakdown = categoryBreakdown,
                topCategory = topCategory,
                averageDailySpending = averageDailySpending,
                comparisonWithLastMonth = comparison,
                mostExpensiveDay = mostExpensiveDayDate,
                mostExpensiveDayAmount = mostExpensiveDayAmount
            )
            
            Result.Success(insight)
            
        } catch (e: Exception) {
            Result.Failure(AppError.fromThrowable(e))
        }
    }
    
    private fun createEmptyInsight(month: Date): MonthlyInsight {
        return MonthlyInsight(
            month = month,
            totalSpent = 0.0,
            categoryBreakdown = emptyList(),
            topCategory = ExpenseCategory.OTHER,
            averageDailySpending = 0.0,
            comparisonWithLastMonth = null,
            mostExpensiveDay = null,
            mostExpensiveDayAmount = 0.0
        )
    }
    
    private suspend fun calculateComparison(
        currentMonth: Date,
        currentTotal: Double
    ): SpendingComparison? {
        return try {
            val calendar = Calendar.getInstance()
            calendar.time = currentMonth
            calendar.add(Calendar.MONTH, -1)
            
            // Início e fim do mês anterior
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            val startOfPrevMonth = calendar.time
            
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
            calendar.set(Calendar.HOUR_OF_DAY, 23)
            calendar.set(Calendar.MINUTE, 59)
            calendar.set(Calendar.SECOND, 59)
            val endOfPrevMonth = calendar.time
            
            val prevExpenses = expenseRepository.getExpensesByDateRange(startOfPrevMonth, endOfPrevMonth).first()
            val previousTotal = prevExpenses.fold(0.0) { acc, expense -> acc + expense.amount }
            
            if (previousTotal == 0.0) return null
            
            val difference = currentTotal - previousTotal
            val percentageDiff = ((difference / previousTotal) * 100).toFloat()
            
            val trend = when {
                abs(percentageDiff) < 5f -> SpendingTrend.STABLE
                difference > 0 -> SpendingTrend.INCREASING
                else -> SpendingTrend.DECREASING
            }
            
            SpendingComparison(
                previousAmount = previousTotal,
                currentAmount = currentTotal,
                differenceAmount = difference,
                differencePercentage = percentageDiff,
                trend = trend
            )
        } catch (e: Exception) {
            null
        }
    }
}
