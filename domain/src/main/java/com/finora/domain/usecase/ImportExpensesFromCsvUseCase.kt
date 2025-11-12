package com.finora.domain.usecase

import com.finora.core.Result
import com.finora.core.AppError
import com.finora.domain.model.Expense
import com.finora.domain.model.ExpenseCategory
import com.finora.domain.repository.ExpenseRepository
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class ImportExpensesFromCsvUseCase @Inject constructor(
    private val expenseRepository: ExpenseRepository
) {
    data class Params(val inputFile: File)
    
    suspend operator fun invoke(params: Params): Result<Int, AppError> {
        return try {
            val lines = params.inputFile.readLines()
            if (lines.isEmpty() || lines.first() != "Date,Amount,Category,Merchant,Description,Notes") {
                return Result.Failure(AppError.ParseError("Invalid CSV format"))
            }
            
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
            var imported = 0
            
            lines.drop(1).forEach { line ->
                val parts = line.split(",")
                if (parts.size >= 6) {
                    try {
                        val date = dateFormat.parse(parts[0])
                        if (date == null) return@forEach
                        
                        val expense = Expense(
                            date = date,
                            amount = parts[1].toDouble(),
                            category = ExpenseCategory.valueOf(parts[2]),
                            merchant = parts[3].ifBlank { null },
                            description = parts[4],
                            notes = parts[5].ifBlank { null }
                        )
                        
                        expenseRepository.saveExpense(expense)
                        imported++
                    } catch (e: Exception) {
                        // Skip invalid lines
                    }
                }
            }
            
            Result.Success(imported)
        } catch (e: Exception) {
            Result.Failure(AppError.FileError("Failed to import CSV: ${e.message}"))
        }
    }
}
