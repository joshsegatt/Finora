package com.finora.features.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finora.core.Logger
import com.finora.domain.model.Expense
import com.finora.domain.model.ExpenseCategory
import com.finora.domain.usecase.DeleteExpenseUseCase
import com.finora.domain.usecase.GetAllExpensesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenseListViewModel @Inject constructor(
    private val getAllExpensesUseCase: GetAllExpensesUseCase,
    private val deleteExpenseUseCase: DeleteExpenseUseCase
) : ViewModel() {
    
    private val _selectedCategory = MutableStateFlow<ExpenseCategory?>(null)
    private val _searchQuery = MutableStateFlow("")
    
    val uiState: StateFlow<ExpenseListUiState> = combine(
        getAllExpensesUseCase.execute(Unit),
        _selectedCategory,
        _searchQuery
    ) { expenses, category, query ->
        var filteredExpenses = expenses
        
        if (category != null) {
            filteredExpenses = filteredExpenses.filter { it.category == category }
        }
        
        if (query.isNotBlank()) {
            filteredExpenses = filteredExpenses.filter {
                it.description.contains(query, ignoreCase = true) ||
                it.merchant?.contains(query, ignoreCase = true) == true ||
                it.notes?.contains(query, ignoreCase = true) == true
            }
        }
        
        ExpenseListUiState(
            expenses = filteredExpenses,
            selectedCategory = category,
            searchQuery = query,
            totalAmount = filteredExpenses.sumOf { it.amount }
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ExpenseListUiState()
        )
    
    fun onCategorySelected(category: ExpenseCategory?) {
        _selectedCategory.value = category
    }
    
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }
    
    fun deleteExpense(expenseId: String) {
        viewModelScope.launch {
            Logger.d("Deleting expense: $expenseId")
            deleteExpenseUseCase(DeleteExpenseUseCase.Params(expenseId))
        }
    }
}

data class ExpenseListUiState(
    val expenses: List<Expense> = emptyList(),
    val selectedCategory: ExpenseCategory? = null,
    val searchQuery: String = "",
    val totalAmount: Double = 0.0
)
