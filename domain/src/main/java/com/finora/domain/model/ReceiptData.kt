package com.finora.domain.model

data class ReceiptData(
    val rawText: String,
    val amount: Double?,
    val date: java.util.Date?,
    val merchant: String?,
    val items: List<String> = emptyList(),
    val inferredCategory: ExpenseCategory = ExpenseCategory.OTHER,
    val confidence: Float = 0f
) {
    fun toExpense(): Expense? {
        if (amount == null || amount <= 0) return null
        
        return Expense(
            amount = amount,
            category = inferredCategory,
            description = merchant ?: "Expense from receipt",
            date = date ?: java.util.Date(),
            merchant = merchant
        )
    }
}
