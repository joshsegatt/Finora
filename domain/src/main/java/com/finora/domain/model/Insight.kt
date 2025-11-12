package com.finora.domain.model

import java.util.Date

/**
 * MonthlyInsight - Análise mensal de gastos.
 */
data class MonthlyInsight(
    val month: Date,
    val totalSpent: Double,
    val categoryBreakdown: List<CategorySpending>,
    val topCategory: ExpenseCategory,
    val averageDailySpending: Double,
    val comparisonWithLastMonth: SpendingComparison?,
    val mostExpensiveDay: Date?,
    val mostExpensiveDayAmount: Double
)

/**
 * CategorySpending - Gastos por categoria.
 */
data class CategorySpending(
    val category: ExpenseCategory,
    val amount: Double,
    val percentage: Float,
    val count: Int
)

/**
 * SpendingComparison - Comparação entre períodos.
 */
data class SpendingComparison(
    val previousAmount: Double,
    val currentAmount: Double,
    val differenceAmount: Double,
    val differencePercentage: Float,
    val trend: SpendingTrend
)

enum class SpendingTrend {
    INCREASING,
    DECREASING,
    STABLE
}

/**
 * SpendingPattern - Padrões detectados.
 */
data class SpendingPattern(
    val type: PatternType,
    val description: String,
    val confidence: Float
)

enum class PatternType {
    WEEKDAY_PATTERN,
    WEEKEND_PATTERN,
    RECURRING_EXPENSE,
    UNUSUAL_SPIKE,
    CONSISTENT_CATEGORY
}

/**
 * MonthlyPrediction - Previsão de gastos.
 */
data class MonthlyPrediction(
    val predictedAmount: Double,
    val confidence: Float,
    val basedOnMonths: Int,
    val recommendation: String?
)
