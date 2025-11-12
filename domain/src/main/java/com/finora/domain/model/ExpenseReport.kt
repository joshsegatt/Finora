package com.finora.domain.model

import java.util.Date

data class ExpenseReport(
    val period: ReportPeriod,
    val totalAmount: Double,
    val expenseCount: Int,
    val categoryBreakdown: Map<ExpenseCategory, CategoryStats>,
    val dailyTrend: List<DailyExpense>,
    val topExpenses: List<Expense>
)

data class CategoryStats(
    val category: ExpenseCategory,
    val totalAmount: Double,
    val percentage: Float,
    val count: Int
)

data class DailyExpense(
    val date: Date,
    val totalAmount: Double,
    val count: Int
)

enum class ReportPeriod(val displayName: String) {
    DAILY("Today"),
    WEEKLY("Week"),
    MONTHLY("Month"),
    YEARLY("Year"),
    ALL_TIME("All Time"),
    CUSTOM("Custom Range")
}
