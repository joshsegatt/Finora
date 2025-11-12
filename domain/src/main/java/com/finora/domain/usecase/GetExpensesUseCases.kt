package com.finora.domain.usecase

import com.finora.core.DispatcherProvider
import com.finora.domain.model.Expense
import com.finora.domain.model.ExpenseCategory
import com.finora.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import java.util.Date
import javax.inject.Inject

class GetAllExpensesUseCase @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    dispatcherProvider: DispatcherProvider
) : FlowUseCase<Unit, Flow<List<Expense>>>(dispatcherProvider) {
    
    override fun execute(params: Unit): Flow<List<Expense>> {
        return expenseRepository.getAllExpenses()
            .flowOn(dispatcherProvider.io)
    }
}

class GetExpensesByCategoryUseCase @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    dispatcherProvider: DispatcherProvider
) : FlowUseCase<GetExpensesByCategoryUseCase.Params, Flow<List<Expense>>>(dispatcherProvider) {
    
    data class Params(val category: ExpenseCategory)
    
    override fun execute(params: Params): Flow<List<Expense>> {
        return expenseRepository.getExpensesByCategory(params.category)
            .flowOn(dispatcherProvider.io)
    }
}

class GetExpensesByDateRangeUseCase @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    dispatcherProvider: DispatcherProvider
) : FlowUseCase<GetExpensesByDateRangeUseCase.Params, Flow<List<Expense>>>(dispatcherProvider) {
    
    data class Params(val startDate: Date, val endDate: Date)
    
    override fun execute(params: Params): Flow<List<Expense>> {
        return expenseRepository.getExpensesByDateRange(params.startDate, params.endDate)
            .flowOn(dispatcherProvider.io)
    }
}
