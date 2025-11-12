package com.finora.features.budgets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finora.core.Logger
import com.finora.core.Result
import com.finora.domain.model.Budget
import com.finora.domain.model.BudgetPeriod
import com.finora.domain.model.ExpenseCategory
import com.finora.domain.usecase.SaveBudgetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddBudgetViewModel @Inject constructor(
    private val saveBudgetUseCase: SaveBudgetUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AddBudgetUiState())
    val uiState: StateFlow<AddBudgetUiState> = _uiState.asStateFlow()
    
    fun onCategoryChanged(category: ExpenseCategory) {
        _uiState.update { it.copy(category = category) }
    }
    
    fun onLimitAmountChanged(amount: String) {
        _uiState.update { it.copy(limitAmount = amount) }
    }
    
    fun onPeriodChanged(period: BudgetPeriod) {
        _uiState.update { it.copy(period = period) }
    }
    
    fun saveBudget(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val state = _uiState.value
            val limitValue = state.limitAmount.toDoubleOrNull()
            
            if (limitValue == null || limitValue <= 0) {
                _uiState.update { it.copy(error = "Valor inválido") }
                return@launch
            }
            
            val now = Date()
            val calendar = Calendar.getInstance()
            calendar.time = now
            
            // Calcular data final baseado no período
            val endDate = when (state.period) {
                BudgetPeriod.MONTHLY -> {
                    calendar.add(Calendar.MONTH, 1)
                    calendar.time
                }
                BudgetPeriod.ANNUAL -> {
                    calendar.add(Calendar.YEAR, 1)
                    calendar.time
                }
            }
            
            val budget = Budget(
                category = state.category,
                limitAmount = limitValue,
                period = state.period,
                startDate = now,
                endDate = endDate
            )
            
            _uiState.update { it.copy(isSaving = true, error = null) }
            
            when (val result = saveBudgetUseCase(SaveBudgetUseCase.Params(budget))) {
                is Result.Success -> {
                    Logger.d("Budget saved successfully")
                    _uiState.update { it.copy(isSaving = false) }
                    onSuccess()
                }
                is Result.Failure -> {
                    Logger.e("Failed to save budget: ${result.error.message}")
                    _uiState.update {
                        it.copy(
                            isSaving = false,
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

data class AddBudgetUiState(
    val category: ExpenseCategory = ExpenseCategory.FOOD,
    val limitAmount: String = "",
    val period: BudgetPeriod = BudgetPeriod.MONTHLY,
    val isSaving: Boolean = false,
    val error: String? = null
)
