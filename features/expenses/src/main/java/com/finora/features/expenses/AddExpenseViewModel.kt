package com.finora.features.expenses

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finora.core.Logger
import com.finora.core.Result
import com.finora.domain.model.Expense
import com.finora.domain.model.ExpenseCategory
import com.finora.domain.model.ReceiptData
import com.finora.domain.usecase.ScanReceiptUseCase
import com.finora.domain.usecase.SaveExpenseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AddExpenseViewModel @Inject constructor(
    private val scanReceiptUseCase: ScanReceiptUseCase,
    private val saveExpenseUseCase: SaveExpenseUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AddExpenseUiState())
    val uiState: StateFlow<AddExpenseUiState> = _uiState.asStateFlow()
    
    fun onImageCaptured(uri: Uri) {
        Logger.d("Image captured: $uri")
        _uiState.update { it.copy(capturedImageUri = uri, isProcessing = true) }
        processReceipt(uri)
    }
    
    private fun processReceipt(imageUri: Uri) {
        viewModelScope.launch {
            _uiState.update { it.copy(isProcessing = true, error = null) }
            
            when (val result = scanReceiptUseCase(ScanReceiptUseCase.Params(imageUri))) {
                is Result.Success -> {
                    val receiptData = result.data
                    Logger.d("Receipt processed successfully: ${receiptData.amount}")
                    _uiState.update {
                        it.copy(
                            isProcessing = false,
                            receiptData = receiptData,
                            amount = receiptData.amount?.toString() ?: "",
                            category = receiptData.inferredCategory,
                            merchant = receiptData.merchant ?: "",
                            date = receiptData.date ?: Date(),
                            showConfidenceWarning = receiptData.confidence < 0.5f
                        )
                    }
                }
                is Result.Failure -> {
                    Logger.e("Failed to process receipt: ${result.error.message}")
                    _uiState.update {
                        it.copy(
                            isProcessing = false,
                            error = result.error.message
                        )
                    }
                }
            }
        }
    }
    
    fun onAmountChanged(amount: String) {
        _uiState.update { it.copy(amount = amount) }
    }
    
    fun onCategoryChanged(category: ExpenseCategory) {
        _uiState.update { it.copy(category = category) }
    }
    
    fun onMerchantChanged(merchant: String) {
        _uiState.update { it.copy(merchant = merchant) }
    }
    
    fun onDescriptionChanged(description: String) {
        _uiState.update { it.copy(description = description) }
    }
    
    fun onDateChanged(date: Date) {
        _uiState.update { it.copy(date = date) }
    }
    
    fun onNotesChanged(notes: String) {
        _uiState.update { it.copy(notes = notes) }
    }
    
    fun saveExpense(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val state = _uiState.value
            val amountValue = state.amount.toDoubleOrNull()
            
            if (amountValue == null || amountValue <= 0) {
                _uiState.update { it.copy(error = "Invalid amount") }
                return@launch
            }
            
            val description = state.description.ifBlank { 
                state.merchant.ifBlank { "Expense" }
            }
            
            val expense = Expense(
                amount = amountValue,
                category = state.category,
                description = description,
                date = state.date,
                merchant = state.merchant.ifBlank { null },
                receiptImagePath = state.capturedImageUri?.toString(),
                notes = state.notes.ifBlank { null }
            )
            
            _uiState.update { it.copy(isSaving = true, error = null) }
            
            when (val result = saveExpenseUseCase(SaveExpenseUseCase.Params(expense))) {
                is Result.Success -> {
                    Logger.d("Expense saved successfully")
                    _uiState.update { it.copy(isSaving = false) }
                    onSuccess()
                }
                is Result.Failure -> {
                    Logger.e("Failed to save expense: ${result.error.message}")
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

data class AddExpenseUiState(
    val capturedImageUri: Uri? = null,
    val isProcessing: Boolean = false,
    val isSaving: Boolean = false,
    val receiptData: ReceiptData? = null,
    val amount: String = "",
    val category: ExpenseCategory = ExpenseCategory.OTHER,
    val merchant: String = "",
    val description: String = "",
    val date: Date = Date(),
    val notes: String = "",
    val error: String? = null,
    val showConfidenceWarning: Boolean = false
)
