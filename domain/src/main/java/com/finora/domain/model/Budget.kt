package com.finora.domain.model

import java.util.Date

/**
 * Budget - Orçamento para controle de gastos.
 * 
 * DESIGN:
 * - Pode ser MONTHLY (mensal) ou ANNUAL (anual)
 * - Vinculado a uma categoria específica
 * - Limite em valor monetário
 * - Período com data início/fim
 */
data class Budget(
    val id: Long = 0,
    val category: ExpenseCategory,
    val limitAmount: Double,
    val period: BudgetPeriod,
    val startDate: Date,
    val endDate: Date,
    val createdAt: Date = Date(),
    val isActive: Boolean = true
)

enum class BudgetPeriod {
    MONTHLY,
    ANNUAL;
    
    companion object {
        fun fromString(value: String): BudgetPeriod {
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: MONTHLY
        }
    }
}

/**
 * BudgetProgress - Status de progresso de um budget.
 */
data class BudgetProgress(
    val budget: Budget,
    val currentSpent: Double,
    val percentage: Float,
    val remaining: Double,
    val status: BudgetStatus
)

enum class BudgetStatus {
    OK,         // < 70%
    WARNING,    // 70% - 100%
    EXCEEDED    // > 100%
}
