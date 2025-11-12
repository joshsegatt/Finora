package com.finora.data.repository

import com.finora.core.AppError
import com.finora.core.Logger
import com.finora.core.Result
import com.finora.data.local.dao.ExpenseDao
import com.finora.data.mapper.toDomain
import com.finora.data.mapper.toEntity
import com.finora.domain.model.Expense
import com.finora.domain.model.ExpenseCategory
import com.finora.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExpenseRepositoryImpl @Inject constructor(
    private val expenseDao: ExpenseDao
) : ExpenseRepository {
    
    override suspend fun saveExpense(expense: Expense): Result<Unit, AppError> {
        return try {
            expenseDao.insert(expense.toEntity())
            Logger.d("Expense saved: ${expense.id}")
            Result.Success(Unit)
        } catch (e: Exception) {
            Logger.e(e, "Failed to save expense")
            Result.Failure(AppError.DatabaseError("Failed to save expense: ${e.message}"))
        }
    }
    
    override suspend fun updateExpense(expense: Expense): Result<Unit, AppError> {
        return try {
            expenseDao.update(expense.toEntity())
            Logger.d("Expense updated: ${expense.id}")
            Result.Success(Unit)
        } catch (e: Exception) {
            Logger.e(e, "Failed to update expense")
            Result.Failure(AppError.DatabaseError("Failed to update expense: ${e.message}"))
        }
    }
    
    override suspend fun deleteExpense(expenseId: String): Result<Unit, AppError> {
        return try {
            expenseDao.deleteById(expenseId)
            Logger.d("Expense deleted: $expenseId")
            Result.Success(Unit)
        } catch (e: Exception) {
            Logger.e(e, "Failed to delete expense")
            Result.Failure(AppError.DatabaseError("Failed to delete expense: ${e.message}"))
        }
    }
    
    override suspend fun getExpenseById(expenseId: String): Result<Expense, AppError> {
        return try {
            val entity = expenseDao.getById(expenseId)
            if (entity != null) {
                Result.Success(entity.toDomain())
            } else {
                Result.Failure(AppError.DatabaseError("Expense not found"))
            }
        } catch (e: Exception) {
            Logger.e(e, "Failed to get expense by id")
            Result.Failure(AppError.DatabaseError("Failed to get expense: ${e.message}"))
        }
    }
    
    override fun getAllExpenses(): Flow<List<Expense>> {
        return expenseDao.getAllExpenses().map { entities ->
            entities.toDomain()
        }
    }
    
    override fun getExpensesByCategory(category: ExpenseCategory): Flow<List<Expense>> {
        return expenseDao.getExpensesByCategory(category.name).map { entities ->
            entities.toDomain()
        }
    }
    
    override fun getExpensesByDateRange(startDate: Date, endDate: Date): Flow<List<Expense>> {
        return expenseDao.getExpensesByDateRange(startDate, endDate).map { entities ->
            entities.toDomain()
        }
    }
    
    override fun getExpensesByCategoryAndDateRange(
        category: String,
        startDate: Date,
        endDate: Date
    ): Flow<List<Expense>> {
        return expenseDao.getExpensesByCategoryAndDateRange(category, startDate, endDate).map { entities ->
            entities.toDomain()
        }
    }
    
    override suspend fun searchExpenses(query: String): Result<List<Expense>, AppError> {
        return try {
            val entities = expenseDao.searchExpenses(query)
            Result.Success(entities.toDomain())
        } catch (e: Exception) {
            Logger.e(e, "Failed to search expenses")
            Result.Failure(AppError.DatabaseError("Failed to search expenses: ${e.message}"))
        }
    }
    
    override suspend fun getTotalExpenses(): Result<Double, AppError> {
        return try {
            val total = expenseDao.getTotalAmount() ?: 0.0
            Result.Success(total)
        } catch (e: Exception) {
            Logger.e(e, "Failed to get total expenses")
            Result.Failure(AppError.DatabaseError("Failed to get total: ${e.message}"))
        }
    }
    
    override suspend fun deleteAllExpenses(): Result<Unit, AppError> {
        return try {
            expenseDao.deleteAll()
            Logger.d("All expenses deleted")
            Result.Success(Unit)
        } catch (e: Exception) {
            Logger.e(e, "Failed to delete all expenses")
            Result.Failure(AppError.DatabaseError("Failed to delete all: ${e.message}"))
        }
    }
}
