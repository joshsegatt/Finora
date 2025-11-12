package com.finora.data.local.dao

import androidx.room.*
import com.finora.data.local.entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface ExpenseDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expense: ExpenseEntity)
    
    @Update
    suspend fun update(expense: ExpenseEntity)
    
    @Delete
    suspend fun delete(expense: ExpenseEntity)
    
    @Query("DELETE FROM expenses WHERE id = :expenseId")
    suspend fun deleteById(expenseId: String)
    
    @Query("SELECT * FROM expenses WHERE id = :expenseId")
    suspend fun getById(expenseId: String): ExpenseEntity?
    
    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>
    
    @Query("SELECT * FROM expenses WHERE category = :category ORDER BY date DESC")
    fun getExpensesByCategory(category: String): Flow<List<ExpenseEntity>>
    
    @Query("SELECT * FROM expenses WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getExpensesByDateRange(startDate: Date, endDate: Date): Flow<List<ExpenseEntity>>
    
    @Query("""
        SELECT * FROM expenses 
        WHERE category = :category 
        AND date BETWEEN :startDate AND :endDate 
        ORDER BY date DESC
    """)
    fun getExpensesByCategoryAndDateRange(
        category: String,
        startDate: Date,
        endDate: Date
    ): Flow<List<ExpenseEntity>>
    
    @Query("""
        SELECT * FROM expenses 
        WHERE description LIKE '%' || :query || '%' 
        OR merchant LIKE '%' || :query || '%' 
        OR notes LIKE '%' || :query || '%'
        ORDER BY date DESC
    """)
    suspend fun searchExpenses(query: String): List<ExpenseEntity>
    
    @Query("SELECT SUM(amount) FROM expenses")
    suspend fun getTotalAmount(): Double?
    
    @Query("DELETE FROM expenses")
    suspend fun deleteAll()
    
    @Query("SELECT COUNT(*) FROM expenses")
    suspend fun getCount(): Int
}
