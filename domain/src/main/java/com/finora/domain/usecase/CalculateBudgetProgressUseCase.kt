package com.finora.domain.usecase

import com.finora.core.AppError
import com.finora.core.DispatcherProvider
import com.finora.core.Result
import com.finora.domain.model.Budget
import com.finora.domain.model.BudgetProgress
import com.finora.domain.model.BudgetStatus
import com.finora.domain.repository.BudgetRepository
import com.finora.domain.repository.ExpenseRepository
import javax.inject.Inject
import kotlin.math.max

/**
 * CalculateBudgetProgressUseCase - Calcula progresso de um budget.
 * 
 * LÓGICA:
 * - Busca todas despesas da categoria no período
 * - Soma total gasto
 * - Calcula % de uso do budget
 * - Define status: OK (<70%), WARNING (70-100%), EXCEEDED (>100%)
 */
class CalculateBudgetProgressUseCase @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val budgetRepository: BudgetRepository,
    dispatcherProvider: DispatcherProvider
) : UseCase<CalculateBudgetProgressUseCase.Params, BudgetProgress>(dispatcherProvider) {
    
    data class Params(val budgetId: Long)
    
    override suspend fun execute(params: Params): Result<BudgetProgress, AppError> {
        return try {
            // Buscar budget
            val budgetResult = budgetRepository.getBudgetById(params.budgetId)
            if (budgetResult is Result.Failure) {
                return Result.Failure(AppError.fromThrowable(budgetResult.error))
            }
            val budget = (budgetResult as Result.Success).data
            
            // Buscar despesas da categoria no período
            val expenses = expenseRepository.getExpensesByCategoryAndDateRange(
                category = budget.category.name,
                startDate = budget.startDate,
                endDate = budget.endDate
            )
            
            // Calcular total gasto (usar fold para evitar ambiguidade)
            var totalSpent = 0.0
            expenses.collect { expenseList ->
                totalSpent = expenseList.fold(0.0) { acc, expense -> acc + expense.amount }
            }
            
            // Calcular progresso
            val progress = calculateProgress(budget, totalSpent)
            
            Result.Success(progress)
            
        } catch (e: Exception) {
            Result.Failure(AppError.fromThrowable(e))
        }
    }
    
    private fun calculateProgress(budget: Budget, spent: Double): BudgetProgress {
        val percentage = if (budget.limitAmount > 0) {
            ((spent / budget.limitAmount) * 100).toFloat()
        } else {
            0f
        }
        
        val remaining = max(0.0, budget.limitAmount - spent)
        
        val status = when {
            percentage < 70f -> BudgetStatus.OK
            percentage <= 100f -> BudgetStatus.WARNING
            else -> BudgetStatus.EXCEEDED
        }
        
        return BudgetProgress(
            budget = budget,
            currentSpent = spent,
            percentage = percentage,
            remaining = remaining,
            status = status
        )
    }
}
