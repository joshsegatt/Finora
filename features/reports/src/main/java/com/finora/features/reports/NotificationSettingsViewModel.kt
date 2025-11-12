package com.finora.features.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finora.domain.model.NotificationConfig
import com.finora.domain.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationSettingsViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(NotificationSettingsUiState())
    val uiState: StateFlow<NotificationSettingsUiState> = _uiState.asStateFlow()
    
    init {
        loadConfig()
    }
    
    private fun loadConfig() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val config = notificationRepository.getNotificationConfig()
                _uiState.value = NotificationSettingsUiState(
                    config = config,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Erro ao carregar configurações"
                )
            }
        }
    }
    
    fun updateBudgetAlerts(enabled: Boolean) {
        viewModelScope.launch {
            val newConfig = _uiState.value.config?.copy(budgetAlertsEnabled = enabled)
            newConfig?.let { saveConfig(it) }
        }
    }
    
    fun updateBudgetThreshold(threshold: Double) {
        viewModelScope.launch {
            val newConfig = _uiState.value.config?.copy(budgetAlertThreshold = threshold)
            newConfig?.let { saveConfig(it) }
        }
    }
    
    fun updateDailySummary(enabled: Boolean) {
        viewModelScope.launch {
            val newConfig = _uiState.value.config?.copy(dailySummaryEnabled = enabled)
            newConfig?.let { saveConfig(it) }
        }
    }
    
    fun updateWeeklySummary(enabled: Boolean) {
        viewModelScope.launch {
            val newConfig = _uiState.value.config?.copy(weeklySummaryEnabled = enabled)
            newConfig?.let { saveConfig(it) }
        }
    }
    
    fun updateMonthlySummary(enabled: Boolean) {
        viewModelScope.launch {
            val newConfig = _uiState.value.config?.copy(monthlySummaryEnabled = enabled)
            newConfig?.let { saveConfig(it) }
        }
    }
    
    fun updateInsightAlerts(enabled: Boolean) {
        viewModelScope.launch {
            val newConfig = _uiState.value.config?.copy(insightAlertsEnabled = enabled)
            newConfig?.let { saveConfig(it) }
        }
    }
    
    fun updateSpendingSensitivity(sensitivity: Double) {
        viewModelScope.launch {
            val newConfig = _uiState.value.config?.copy(spendingSpikeSensitivity = sensitivity)
            newConfig?.let { saveConfig(it) }
        }
    }
    
    private suspend fun saveConfig(config: NotificationConfig) {
        try {
            notificationRepository.saveNotificationConfig(config)
            _uiState.value = _uiState.value.copy(
                config = config,
                showSavedMessage = true
            )
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                error = e.message ?: "Erro ao salvar configurações"
            )
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    fun clearSavedMessage() {
        _uiState.value = _uiState.value.copy(showSavedMessage = false)
    }
}

data class NotificationSettingsUiState(
    val config: NotificationConfig? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val showSavedMessage: Boolean = false
)
