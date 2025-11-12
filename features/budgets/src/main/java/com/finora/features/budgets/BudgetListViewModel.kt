package com.finora.features.budgets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finora.core.Logger
import com.finora.core.Result
import com.finora.domain.model.Budget
import com.finora.domain.model.BudgetProgress
import com.finora.domain.usecase.CalculateBudgetProgressUseCase
import com.finora.domain.usecase.DeleteBudgetUseCase
import com.finora.domain.usecase.GetAllBudgetsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetListViewModel @Inject constructor(
    private val getAllBudgetsUseCase: GetAllBudgetsUseCase,
    private val calculateBudgetProgressUseCase: CalculateBudgetProgressUseCase,
    private val deleteBudgetUseCase: DeleteBudgetUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(BudgetListUiState())
    val uiState: StateFlow<BudgetListUiState> = _uiState.asStateFlow()
    
    init {
        loadBudgets()
    }
    
    private fun loadBudgets() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            getAllBudgetsUseCase.execute(Unit).collect { budgets ->
                // Calcular progresso para cada budget
                val progressList = mutableListOf<BudgetProgress>()
                
                for (budget in budgets) {
                    when (val result = calculateBudgetProgressUseCase(
                        CalculateBudgetProgressUseCase.Params(budget.id)
                    )) {
                        is Result.Success -> progressList.add(result.data)
                        is Result.Failure -> {
                            Logger.e("Failed to calculate progress for budget ${budget.id}")
                        }
                    }
                }
                
                _uiState.update {
                    it.copy(
                        budgetProgress = progressList,
                        isLoading = false
                    )
                }
            }
        }
    }
    
    fun deleteBudget(budgetId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            when (val result = deleteBudgetUseCase(DeleteBudgetUseCase.Params(budgetId))) {
                is Result.Success -> {
                    Logger.d("Budget deleted successfully")
                    loadBudgets() // Recarregar lista
                }
                is Result.Failure -> {
                    Logger.e("Failed to delete budget: ${result.error.message}")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.error.message
                        )
                    }
                }
            }
        }
    }
    
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}

data class BudgetListUiState(
    val budgetProgress: List<BudgetProgress> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
