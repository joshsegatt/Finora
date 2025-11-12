package com.finora.domain.repository

import com.finora.core.AppError
import com.finora.core.Result
import com.finora.domain.model.Expense
import com.finora.domain.model.ExpenseCategory
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface ExpenseRepository {
    
    suspend fun saveExpense(expense: Expense): Result<Unit, AppError>
    
    suspend fun updateExpense(expense: Expense): Result<Unit, AppError>
    
    suspend fun deleteExpense(expenseId: String): Result<Unit, AppError>
    
    suspend fun getExpenseById(expenseId: String): Result<Expense, AppError>
    
    fun getAllExpenses(): Flow<List<Expense>>
    
    fun getExpensesByCategory(category: ExpenseCategory): Flow<List<Expense>>
    
    fun getExpensesByDateRange(startDate: Date, endDate: Date): Flow<List<Expense>>
    
    suspend fun searchExpenses(query: String): Result<List<Expense>, AppError>
    
    suspend fun getTotalExpenses(): Result<Double, AppError>
    
    suspend fun deleteAllExpenses(): Result<Unit, AppError>
}
