package com.finora.domain.usecase

import com.finora.core.AppError
import com.finora.core.DispatcherProvider
import com.finora.core.Result
import com.finora.domain.model.*
import com.finora.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.first
import java.util.*
import javax.inject.Inject

class GenerateReportUseCase @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    dispatcherProvider: DispatcherProvider
) : UseCase<GenerateReportUseCase.Params, ExpenseReport>(dispatcherProvider) {
    
    data class Params(
        val period: ReportPeriod,
        val startDate: Date? = null,
        val endDate: Date? = null
    )
    
    override suspend fun execute(params: Params): Result<ExpenseReport, AppError> {
        val (startDate, endDate) = calculateDateRange(params.period, params.startDate, params.endDate)
        
        val expenses = expenseRepository.getExpensesByDateRange(startDate, endDate).first()
        
        if (expenses.isEmpty()) {
            return Result.Success(createEmptyReport(params.period))
        }
        
        val totalAmount = expenses.sumOf { it.amount }
        val categoryBreakdown = calculateCategoryBreakdown(expenses, totalAmount)
        val dailyTrend = calculateDailyTrend(expenses)
        val topExpenses = expenses.sortedByDescending { it.amount }.take(10)
        
        return Result.Success(
            ExpenseReport(
                period = params.period,
                totalAmount = totalAmount,
                expenseCount = expenses.size,
                categoryBreakdown = categoryBreakdown,
                dailyTrend = dailyTrend,
                topExpenses = topExpenses
            )
        )
    }
    
    private fun calculateDateRange(
        period: ReportPeriod,
        customStart: Date?,
        customEnd: Date?
    ): Pair<Date, Date> {
        val calendar = Calendar.getInstance()
        val endDate = Date()
        
        val startDate = when (period) {
            ReportPeriod.DAILY -> {
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.time
            }
            ReportPeriod.WEEKLY -> {
                calendar.add(Calendar.DAY_OF_YEAR, -7)
                calendar.time
            }
            ReportPeriod.MONTHLY -> {
                calendar.add(Calendar.MONTH, -1)
                calendar.time
            }
            ReportPeriod.YEARLY -> {
                calendar.add(Calendar.YEAR, -1)
                calendar.time
            }
            ReportPeriod.ALL_TIME -> Date(0)
            ReportPeriod.CUSTOM -> customStart ?: Date(0)
        }
        
        return startDate to (if (period == ReportPeriod.CUSTOM) customEnd ?: endDate else endDate)
    }
    
    private fun calculateCategoryBreakdown(
        expenses: List<Expense>,
        totalAmount: Double
    ): Map<ExpenseCategory, CategoryStats> {
        return expenses.groupBy { it.category }
            .mapValues { (category, categoryExpenses) ->
                val categoryTotal = categoryExpenses.sumOf { it.amount }
                CategoryStats(
                    category = category,
                    totalAmount = categoryTotal,
                    percentage = ((categoryTotal / totalAmount) * 100).toFloat(),
                    count = categoryExpenses.size
                )
            }
    }
    
    private fun calculateDailyTrend(expenses: List<Expense>): List<DailyExpense> {
        return expenses.groupBy { expense ->
            Calendar.getInstance().apply {
                time = expense.date
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.time
        }.map { (date, dailyExpenses) ->
            DailyExpense(
                date = date,
                totalAmount = dailyExpenses.sumOf { it.amount },
                count = dailyExpenses.size
            )
        }.sortedBy { it.date }
    }
    
    private fun createEmptyReport(period: ReportPeriod): ExpenseReport {
        return ExpenseReport(
            period = period,
            totalAmount = 0.0,
            expenseCount = 0,
            categoryBreakdown = emptyMap(),
            dailyTrend = emptyList(),
            topExpenses = emptyList()
        )
    }
}
