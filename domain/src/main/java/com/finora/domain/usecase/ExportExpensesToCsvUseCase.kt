package com.finora.domain.usecase

import com.finora.core.Result
import com.finora.core.AppError
import com.finora.domain.model.Expense
import com.finora.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.first
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class ExportExpensesToCsvUseCase @Inject constructor(
    private val expenseRepository: ExpenseRepository
) {
    data class Params(val outputFile: File)
    
    suspend operator fun invoke(params: Params): Result<File, AppError> {
        return try {
            val expenses = expenseRepository.getAllExpenses().first()
            
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
            val csv = buildString {
                appendLine("Date,Amount,Category,Merchant,Description,Notes")
                expenses.forEach { expense ->
                    val date = dateFormat.format(expense.date)
                    val amount = expense.amount
                    val category = expense.category.name
                    val merchant = expense.merchant?.replace(",", ";") ?: ""
                    val description = expense.description.replace(",", ";")
                    val notes = expense.notes?.replace(",", ";") ?: ""
                    appendLine("$date,$amount,$category,$merchant,$description,$notes")
                }
            }
            
            params.outputFile.writeText(csv)
            Result.Success(params.outputFile)
        } catch (e: Exception) {
            Result.Failure(AppError.FileError("Failed to export CSV: ${e.message}"))
        }
    }
}
