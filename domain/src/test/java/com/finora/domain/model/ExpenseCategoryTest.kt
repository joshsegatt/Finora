package com.finora.domain.model

import org.junit.Assert.*
import org.junit.Test

class ExpenseCategoryTest {
    
    @Test
    fun `inferFromText detects food category`() {
        val category = ExpenseCategory.inferFromText("Restaurant ABC")
        assertEquals(ExpenseCategory.FOOD, category)
    }
    
    @Test
    fun `inferFromText detects transportation category`() {
        val category = ExpenseCategory.inferFromText("Uber ride")
        assertEquals(ExpenseCategory.TRANSPORTATION, category)
    }
    
    @Test
    fun `inferFromText detects healthcare category`() {
        val category = ExpenseCategory.inferFromText("Pharmacy Plus")
        assertEquals(ExpenseCategory.HEALTHCARE, category)
    }
    
    @Test
    fun `inferFromText returns OTHER for unknown text`() {
        val category = ExpenseCategory.inferFromText("Random xyz")
        assertEquals(ExpenseCategory.OTHER, category)
    }
    
    @Test
    fun `fromString parses category name`() {
        val category = ExpenseCategory.fromString("FOOD")
        assertEquals(ExpenseCategory.FOOD, category)
    }
    
    @Test
    fun `fromString parses display name`() {
        val category = ExpenseCategory.fromString("Food & Dining")
        assertEquals(ExpenseCategory.FOOD, category)
    }
}
