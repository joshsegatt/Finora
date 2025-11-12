package com.finora.domain.usecase

import com.finora.core.AppError
import com.finora.core.DispatcherProvider
import com.finora.core.Result
import com.finora.domain.model.Expense
import com.finora.domain.repository.ExpenseRepository
import javax.inject.Inject

class SaveExpenseUseCase @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    dispatcherProvider: DispatcherProvider
) : UseCase<SaveExpenseUseCase.Params, Unit>(dispatcherProvider) {
    
    data class Params(val expense: Expense)
    
    override suspend fun execute(params: Params): Result<Unit, AppError> {
        if (!params.expense.isValid()) {
            return Result.Failure(AppError.ValidationError("Invalid expense data"))
        }
        return expenseRepository.saveExpense(params.expense)
    }
}

class UpdateExpenseUseCase @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    dispatcherProvider: DispatcherProvider
) : UseCase<UpdateExpenseUseCase.Params, Unit>(dispatcherProvider) {
    
    data class Params(val expense: Expense)
    
    override suspend fun execute(params: Params): Result<Unit, AppError> {
        if (!params.expense.isValid()) {
            return Result.Failure(AppError.ValidationError("Invalid expense data"))
        }
        return expenseRepository.updateExpense(params.expense)
    }
}

class DeleteExpenseUseCase @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    dispatcherProvider: DispatcherProvider
) : UseCase<DeleteExpenseUseCase.Params, Unit>(dispatcherProvider) {
    
    data class Params(val expenseId: String)
    
    override suspend fun execute(params: Params): Result<Unit, AppError> {
        return expenseRepository.deleteExpense(params.expenseId)
    }
}
