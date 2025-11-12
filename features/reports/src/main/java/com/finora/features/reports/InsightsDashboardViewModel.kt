package com.finora.features.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finora.core.Logger
import com.finora.core.Result
import com.finora.domain.model.MonthlyInsight
import com.finora.domain.model.MonthlyPrediction
import com.finora.domain.usecase.GetMonthlyInsightsUseCase
import com.finora.domain.usecase.PredictMonthlySpendingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class InsightsDashboardViewModel @Inject constructor(
    private val getMonthlyInsightsUseCase: GetMonthlyInsightsUseCase,
    private val predictMonthlySpendingUseCase: PredictMonthlySpendingUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(InsightsDashboardUiState())
    val uiState: StateFlow<InsightsDashboardUiState> = _uiState.asStateFlow()
    
    init {
        loadInsights()
    }
    
    fun loadInsights() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            // Carregar insights do mês atual
            val currentMonth = Date()
            when (val insightsResult = getMonthlyInsightsUseCase(
                GetMonthlyInsightsUseCase.Params(currentMonth)
            )) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            monthlyInsight = insightsResult.data,
                            isLoading = false
                        )
                    }
                }
                is Result.Failure -> {
                    Logger.e("Failed to load insights: ${insightsResult.error.message}")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = insightsResult.error.message
                        )
                    }
                }
            }
            
            // Carregar previsão
            when (val predictionResult = predictMonthlySpendingUseCase(Unit)) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(prediction = predictionResult.data)
                    }
                }
                is Result.Failure -> {
                    Logger.e("Failed to load prediction: ${predictionResult.error.message}")
                }
            }
        }
    }
    
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}

data class InsightsDashboardUiState(
    val monthlyInsight: MonthlyInsight? = null,
    val prediction: MonthlyPrediction? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
