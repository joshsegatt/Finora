package com.finora.domain.model

import java.util.Date
import java.util.UUID

data class Expense(
    val id: String = UUID.randomUUID().toString(),
    val amount: Double,
    val category: ExpenseCategory,
    val description: String,
    val date: Date,
    val merchant: String? = null,
    val receiptImagePath: String? = null,
    val tags: List<String> = emptyList(),
    val notes: String? = null,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
) {
    fun isValid(): Boolean {
        return amount > 0 && description.isNotBlank()
    }
    
    fun withCategory(newCategory: ExpenseCategory): Expense {
        return copy(category = newCategory, updatedAt = Date())
    }
    
    fun withAmount(newAmount: Double): Expense {
        return copy(amount = newAmount, updatedAt = Date())
    }
}
