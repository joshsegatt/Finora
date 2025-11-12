package com.finora.data.mapper

import com.finora.data.local.entity.ExpenseEntity
import com.finora.domain.model.Expense
import com.finora.domain.model.ExpenseCategory

fun Expense.toEntity(): ExpenseEntity {
    return ExpenseEntity(
        id = id,
        amount = amount,
        category = category.name,
        description = description,
        date = date,
        merchant = merchant,
        receiptImagePath = receiptImagePath,
        tags = tags,
        notes = notes,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun ExpenseEntity.toDomain(): Expense {
    return Expense(
        id = id,
        amount = amount,
        category = ExpenseCategory.fromString(category),
        description = description,
        date = date,
        merchant = merchant,
        receiptImagePath = receiptImagePath,
        tags = tags,
        notes = notes,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun List<ExpenseEntity>.toDomain(): List<Expense> {
    return map { it.toDomain() }
}
