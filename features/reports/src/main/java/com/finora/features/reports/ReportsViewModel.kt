package com.finora.features.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finora.core.Logger
import com.finora.core.Result
import com.finora.core.datastore.PreferencesManager
import com.finora.domain.model.ExpenseReport
import com.finora.domain.model.ReportPeriod
import com.finora.domain.usecase.GenerateReportUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val generateReportUseCase: GenerateReportUseCase,
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    
    private val _reportState = MutableStateFlow(ReportsInternalState())
    
    val uiState: StateFlow<ReportsUiState> = combine(
        _reportState,
        preferencesManager.currency
    ) { state, currencyCode ->
        ReportsUiState(
            isLoading = state.isLoading,
            selectedPeriod = state.selectedPeriod,
            report = state.report,
            trendData = state.trendData,
            error = state.error,
            currencyCode = currencyCode
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ReportsUiState()
    )
    
    init {
        loadReport(ReportPeriod.MONTHLY)
    }
    
    fun loadReport(period: ReportPeriod) {
        viewModelScope.launch {
            _reportState.update { it.copy(isLoading = true, selectedPeriod = period) }
            
            when (val result = generateReportUseCase(GenerateReportUseCase.Params(period))) {
                is Result.Success -> {
                    Logger.d("Report generated successfully")
                    val trendData = generateTrendData(result.data, period)
                    _reportState.update {
                        it.copy(
                            isLoading = false,
                            report = result.data,
                            trendData = trendData,
                            error = null
                        )
                    }
                }
                is Result.Failure -> {
                    Logger.e("Failed to generate report: ${result.error.message}")
                    _reportState.update {
                        it.copy(
                            isLoading = false,
                            error = result.error.message
                        )
                    }
                }
            }
        }
    }
    
    private fun generateTrendData(report: ExpenseReport, period: ReportPeriod): List<Pair<String, Double>> {
        val calendar = Calendar.getInstance()
        val dateFormat = when (period) {
            ReportPeriod.DAILY -> java.text.SimpleDateFormat("HH:mm", Locale.getDefault())
            ReportPeriod.WEEKLY -> java.text.SimpleDateFormat("EEE", Locale.getDefault()) // Mon, Tue, Wed
            ReportPeriod.MONTHLY -> java.text.SimpleDateFormat("dd", Locale.getDefault()) // 1, 2, 3...
            ReportPeriod.YEARLY -> java.text.SimpleDateFormat("MMM", Locale.getDefault()) // Jan, Feb, Mar
            else -> java.text.SimpleDateFormat("dd/MM", Locale.getDefault())
        }
        
        return report.dailyTrend.map { dailyExpense ->
            val label = dateFormat.format(dailyExpense.date)
            label to dailyExpense.totalAmount
        }
    }
    
    fun exportToCsv(onComplete: (String) -> Unit) {
        viewModelScope.launch {
            val report = _reportState.value.report ?: return@launch
            
            val csv = buildString {
                appendLine("Date,Category,Description,Merchant,Amount")
                report.topExpenses.forEach { expense ->
                    appendLine(
                        "${expense.date},${expense.category.displayName}," +
                        "\"${expense.description}\",\"${expense.merchant ?: ""}\",${expense.amount}"
                    )
                }
            }
            
            onComplete(csv)
        }
    }
    
    fun exportToJson(onComplete: (String) -> Unit) {
        viewModelScope.launch {
            val report = _reportState.value.report ?: return@launch
            
            val json = buildString {
                appendLine("{")
                appendLine("  \"period\": \"${report.period.displayName}\",")
                appendLine("  \"totalAmount\": ${report.totalAmount},")
                appendLine("  \"expenseCount\": ${report.expenseCount},")
                appendLine("  \"expenses\": [")
                
                report.topExpenses.forEachIndexed { index, expense ->
                    appendLine("    {")
                    appendLine("      \"id\": \"${expense.id}\",")
                    appendLine("      \"amount\": ${expense.amount},")
                    appendLine("      \"category\": \"${expense.category.displayName}\",")
                    appendLine("      \"description\": \"${expense.description}\",")
                    appendLine("      \"merchant\": \"${expense.merchant ?: ""}\",")
                    appendLine("      \"date\": \"${expense.date}\"")
                    append("    }")
                    if (index < report.topExpenses.size - 1) appendLine(",")
                    else appendLine()
                }
                
                appendLine("  ]")
                appendLine("}")
            }
            
            onComplete(json)
        }
    }
}

// Internal state (without currency)
private data class ReportsInternalState(
    val isLoading: Boolean = false,
    val report: ExpenseReport? = null,
    val selectedPeriod: ReportPeriod = ReportPeriod.MONTHLY,
    val error: String? = null,
    val trendData: List<Pair<String, Double>> = emptyList()
)

// Public UI state (with currency)
data class ReportsUiState(
    val isLoading: Boolean = false,
    val report: ExpenseReport? = null,
    val selectedPeriod: ReportPeriod = ReportPeriod.MONTHLY,
    val error: String? = null,
    val trendData: List<Pair<String, Double>> = emptyList(),
    val currencyCode: String = "GBP"
)
