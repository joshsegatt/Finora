package com.finora.data.mapper

import com.finora.data.local.entity.ExpenseEntity
import com.finora.domain.model.Expense
import com.finora.domain.model.ExpenseCategory
import org.junit.Assert.*
import org.junit.Test
import java.util.Date

class ExpenseMapperTest {
    
    @Test
    fun `maps domain Expense to ExpenseEntity`() {
        val expense = Expense(
            id = "test-id",
            amount = 100.0,
            category = ExpenseCategory.FOOD,
            description = "Test expense",
            date = Date(),
            merchant = "Test Store",
            tags = listOf("tag1", "tag2")
        )
        
        val entity = expense.toEntity()
        
        assertEquals(expense.id, entity.id)
        assertEquals(expense.amount, entity.amount, 0.01)
        assertEquals(expense.category.name, entity.category)
        assertEquals(expense.description, entity.description)
        assertEquals(expense.merchant, entity.merchant)
        assertEquals(expense.tags, entity.tags)
    }
    
    @Test
    fun `maps ExpenseEntity to domain Expense`() {
        val entity = ExpenseEntity(
            id = "test-id",
            amount = 50.0,
            category = "SHOPPING",
            description = "Test entity",
            date = Date(),
            merchant = "Store",
            receiptImagePath = null,
            tags = listOf("tag"),
            notes = "Note",
            createdAt = Date(),
            updatedAt = Date()
        )
        
        val expense = entity.toDomain()
        
        assertEquals(entity.id, expense.id)
        assertEquals(entity.amount, expense.amount, 0.01)
        assertEquals(ExpenseCategory.SHOPPING, expense.category)
        assertEquals(entity.description, expense.description)
        assertEquals(entity.merchant, expense.merchant)
        assertEquals(entity.tags, expense.tags)
    }
    
    @Test
    fun `maps list of entities to domain`() {
        val entities = listOf(
            ExpenseEntity(
                id = "1",
                amount = 10.0,
                category = "FOOD",
                description = "Test 1",
                date = Date(),
                merchant = null,
                receiptImagePath = null,
                tags = emptyList(),
                notes = null,
                createdAt = Date(),
                updatedAt = Date()
            ),
            ExpenseEntity(
                id = "2",
                amount = 20.0,
                category = "TRANSPORTATION",
                description = "Test 2",
                date = Date(),
                merchant = null,
                receiptImagePath = null,
                tags = emptyList(),
                notes = null,
                createdAt = Date(),
                updatedAt = Date()
            )
        )
        
        val expenses = entities.toDomain()
        
        assertEquals(2, expenses.size)
        assertEquals("1", expenses[0].id)
        assertEquals("2", expenses[1].id)
    }
}
