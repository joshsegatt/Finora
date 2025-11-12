package com.finora.domain.repository

import com.finora.core.Result
import com.finora.domain.model.Budget
import kotlinx.coroutines.flow.Flow

/**
 * BudgetRepository - Interface para CRUD de budgets.
 */
interface BudgetRepository {
    
    fun getAllActiveBudgets(): Flow<List<Budget>>
    
    suspend fun getBudgetById(id: Long): Result<Budget, Throwable>
    
    suspend fun getBudgetByCategory(category: String): Result<Budget?, Throwable>
    
    suspend fun saveBudget(budget: Budget): Result<Long, Throwable>
    
    suspend fun deleteBudget(id: Long): Result<Unit, Throwable>
}
