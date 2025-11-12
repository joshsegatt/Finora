package com.finora.domain.model

import org.junit.Assert.*
import org.junit.Test
import java.util.Date

class ExpenseTest {
    
    @Test
    fun `valid expense returns true`() {
        val expense = Expense(
            amount = 100.0,
            category = ExpenseCategory.FOOD,
            description = "Dinner",
            date = Date()
        )
        
        assertTrue(expense.isValid())
    }
    
    @Test
    fun `expense with negative amount is invalid`() {
        val expense = Expense(
            amount = -50.0,
            category = ExpenseCategory.FOOD,
            description = "Test",
            date = Date()
        )
        
        assertFalse(expense.isValid())
    }
    
    @Test
    fun `expense with blank description is invalid`() {
        val expense = Expense(
            amount = 50.0,
            category = ExpenseCategory.FOOD,
            description = "   ",
            date = Date()
        )
        
        assertFalse(expense.isValid())
    }
    
    @Test
    fun `withCategory updates category and timestamp`() {
        val expense = Expense(
            amount = 50.0,
            category = ExpenseCategory.FOOD,
            description = "Test",
            date = Date()
        )
        
        Thread.sleep(10)
        val updated = expense.withCategory(ExpenseCategory.SHOPPING)
        
        assertEquals(ExpenseCategory.SHOPPING, updated.category)
        assertTrue(updated.updatedAt.after(expense.updatedAt))
    }
}
