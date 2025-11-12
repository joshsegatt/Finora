package com.finora.data.local.dao

import androidx.room.*
import com.finora.data.local.entity.BudgetEntity
import kotlinx.coroutines.flow.Flow

/**
 * BudgetDao - CRUD para budgets.
 */
@Dao
interface BudgetDao {
    
    @Query("SELECT * FROM budgets WHERE isActive = 1 ORDER BY createdAt DESC")
    fun getAllActiveBudgets(): Flow<List<BudgetEntity>>
    
    @Query("SELECT * FROM budgets WHERE id = :id")
    suspend fun getBudgetById(id: Long): BudgetEntity?
    
    @Query("SELECT * FROM budgets WHERE category = :category AND isActive = 1")
    suspend fun getBudgetByCategory(category: String): BudgetEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(budget: BudgetEntity): Long
    
    @Update
    suspend fun updateBudget(budget: BudgetEntity)
    
    @Delete
    suspend fun deleteBudget(budget: BudgetEntity)
    
    @Query("UPDATE budgets SET isActive = 0 WHERE id = :id")
    suspend fun deactivateBudget(id: Long)
}
