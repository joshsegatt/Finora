package com.finora.data.repository

import com.finora.core.Result
import com.finora.data.local.dao.BudgetDao
import com.finora.data.local.entity.BudgetEntity
import com.finora.domain.model.Budget
import com.finora.domain.model.BudgetPeriod
import com.finora.domain.model.ExpenseCategory
import com.finora.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * BudgetRepositoryImpl - Implementação com Room.
 */
class BudgetRepositoryImpl @Inject constructor(
    private val budgetDao: BudgetDao
) : BudgetRepository {
    
    override fun getAllActiveBudgets(): Flow<List<Budget>> {
        return budgetDao.getAllActiveBudgets().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override suspend fun getBudgetById(id: Long): Result<Budget, Throwable> {
        return try {
            val entity = budgetDao.getBudgetById(id)
            if (entity != null) {
                Result.Success(entity.toDomain())
            } else {
                Result.Failure(NoSuchElementException("Budget not found"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
    
    override suspend fun getBudgetByCategory(category: String): Result<Budget?, Throwable> {
        return try {
            val entity = budgetDao.getBudgetByCategory(category)
            Result.Success(entity?.toDomain())
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
    
    override suspend fun saveBudget(budget: Budget): Result<Long, Throwable> {
        return try {
            val id = budgetDao.insertBudget(budget.toEntity())
            Result.Success(id)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
    
    override suspend fun deleteBudget(id: Long): Result<Unit, Throwable> {
        return try {
            budgetDao.deactivateBudget(id)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
    
    private fun BudgetEntity.toDomain(): Budget {
        return Budget(
            id = id,
            category = ExpenseCategory.fromString(category),
            limitAmount = limitAmount,
            period = BudgetPeriod.fromString(period),
            startDate = startDate,
            endDate = endDate,
            createdAt = createdAt,
            isActive = isActive
        )
    }
    
    private fun Budget.toEntity(): BudgetEntity {
        return BudgetEntity(
            id = id,
            category = category.name,
            limitAmount = limitAmount,
            period = period.name,
            startDate = startDate,
            endDate = endDate,
            createdAt = createdAt,
            isActive = isActive
        )
    }
}
