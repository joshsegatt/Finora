package com.finora.domain.usecase

import com.finora.core.AppError
import com.finora.core.DispatcherProvider
import com.finora.core.Result
import com.finora.domain.model.Budget
import com.finora.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * GetAllBudgetsUseCase - Retorna todos budgets ativos.
 */
class GetAllBudgetsUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository,
    dispatcherProvider: DispatcherProvider
) : FlowUseCase<Unit, Flow<List<Budget>>>(dispatcherProvider) {
    
    override fun execute(params: Unit): Flow<List<Budget>> {
        return budgetRepository.getAllActiveBudgets()
    }
}

/**
 * SaveBudgetUseCase - Salva/atualiza budget.
 */
class SaveBudgetUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository,
    dispatcherProvider: DispatcherProvider
) : UseCase<SaveBudgetUseCase.Params, Long>(dispatcherProvider) {
    
    data class Params(val budget: Budget)
    
    override suspend fun execute(params: Params): Result<Long, AppError> {
        return when (val result = budgetRepository.saveBudget(params.budget)) {
            is Result.Success -> Result.Success(result.data)
            is Result.Failure -> Result.Failure(AppError.fromThrowable(result.error))
        }
    }
}

/**
 * DeleteBudgetUseCase - Deleta/desativa budget.
 */
class DeleteBudgetUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository,
    dispatcherProvider: DispatcherProvider
) : UseCase<DeleteBudgetUseCase.Params, Unit>(dispatcherProvider) {
    
    data class Params(val budgetId: Long)
    
    override suspend fun execute(params: Params): Result<Unit, AppError> {
        return when (val result = budgetRepository.deleteBudget(params.budgetId)) {
            is Result.Success -> Result.Success(Unit)
            is Result.Failure -> Result.Failure(AppError.fromThrowable(result.error))
        }
    }
}

/**
 * GetBudgetByCategoryUseCase - Busca budget de uma categoria.
 */
class GetBudgetByCategoryUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository,
    dispatcherProvider: DispatcherProvider
) : UseCase<GetBudgetByCategoryUseCase.Params, Budget?>(dispatcherProvider) {
    
    data class Params(val category: String)
    
    override suspend fun execute(params: Params): Result<Budget?, AppError> {
        return when (val result = budgetRepository.getBudgetByCategory(params.category)) {
            is Result.Success -> Result.Success(result.data)
            is Result.Failure -> Result.Failure(AppError.fromThrowable(result.error))
        }
    }
}
