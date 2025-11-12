package com.finora.features.reports

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.finora.ui.theme.FinoraCard
import com.finora.ui.theme.FinoraSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationSettingsScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: NotificationSettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configurações de Notificações") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        },
        snackbarHost = {
            if (uiState.showSavedMessage) {
                LaunchedEffect(Unit) {
                    kotlinx.coroutines.delay(2000)
                    viewModel.clearSavedMessage()
                }
                Snackbar(
                    modifier = Modifier.padding(FinoraSpacing.medium)
                ) {
                    Text("✓ Configurações salvas")
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                
                uiState.config != null -> {
                    NotificationSettingsContent(
                        config = uiState.config!!,
                        onBudgetAlertsChange = viewModel::updateBudgetAlerts,
                        onBudgetThresholdChange = viewModel::updateBudgetThreshold,
                        onDailySummaryChange = viewModel::updateDailySummary,
                        onWeeklySummaryChange = viewModel::updateWeeklySummary,
                        onMonthlySummaryChange = viewModel::updateMonthlySummary,
                        onInsightAlertsChange = viewModel::updateInsightAlerts,
                        onSpendingSensitivityChange = viewModel::updateSpendingSensitivity
                    )
                }
            }
        }
    }
    
    uiState.error?.let { error ->
        AlertDialog(
            onDismissRequest = viewModel::clearError,
            title = { Text("Erro") },
            text = { Text(error) },
            confirmButton = {
                TextButton(onClick = viewModel::clearError) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
private fun NotificationSettingsContent(
    config: com.finora.domain.model.NotificationConfig,
    onBudgetAlertsChange: (Boolean) -> Unit,
    onBudgetThresholdChange: (Double) -> Unit,
    onDailySummaryChange: (Boolean) -> Unit,
    onWeeklySummaryChange: (Boolean) -> Unit,
    onMonthlySummaryChange: (Boolean) -> Unit,
    onInsightAlertsChange: (Boolean) -> Unit,
    onSpendingSensitivityChange: (Double) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(FinoraSpacing.medium),
        verticalArrangement = Arrangement.spacedBy(FinoraSpacing.medium)
    ) {
        // Seção: Alertas de Orçamento
        item {
            Text(
                text = "Alertas de Orçamento",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = FinoraSpacing.small)
            )
        }
        
        item {
            FinoraCard {
                Column(
                    modifier = Modifier.padding(FinoraSpacing.medium),
                    verticalArrangement = Arrangement.spacedBy(FinoraSpacing.small)
                ) {
                    SwitchSetting(
                        title = "Ativar Alertas de Orçamento",
                        subtitle = "Receba notificações quando se aproximar do limite",
                        checked = config.budgetAlertsEnabled,
                        onCheckedChange = onBudgetAlertsChange
                    )
                    
                    if (config.budgetAlertsEnabled) {
                        Divider(modifier = Modifier.padding(vertical = FinoraSpacing.small))
                        
                        Text(
                            text = "Alerta em ${(config.budgetAlertThreshold * 100).toInt()}% do limite",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        
                        Slider(
                            value = config.budgetAlertThreshold.toFloat(),
                            onValueChange = { onBudgetThresholdChange(it.toDouble()) },
                            valueRange = 0.5f..0.95f,
                            steps = 8
                        )
                    }
                }
            }
        }
        
        // Seção: Resumos
        item {
            Text(
                text = "Resumos Automáticos",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = FinoraSpacing.medium)
            )
        }
        
        item {
            FinoraCard {
                Column(
                    modifier = Modifier.padding(FinoraSpacing.medium),
                    verticalArrangement = Arrangement.spacedBy(FinoraSpacing.medium)
                ) {
                    SwitchSetting(
                        title = "Resumo Diário",
                        subtitle = "Receba um resumo dos seus gastos todos os dias às ${config.dailySummaryTime}",
                        checked = config.dailySummaryEnabled,
                        onCheckedChange = onDailySummaryChange
                    )
                    
                    Divider()
                    
                    SwitchSetting(
                        title = "Resumo Semanal",
                        subtitle = "Toda segunda-feira às ${config.weeklySummaryTime}",
                        checked = config.weeklySummaryEnabled,
                        onCheckedChange = onWeeklySummaryChange
                    )
                    
                    Divider()
                    
                    SwitchSetting(
                        title = "Resumo Mensal",
                        subtitle = "Todo dia ${config.monthlySummaryDay} às ${config.monthlySummaryTime}",
                        checked = config.monthlySummaryEnabled,
                        onCheckedChange = onMonthlySummaryChange
                    )
                }
            }
        }
        
        // Seção: Insights
        item {
            Text(
                text = "Insights Inteligentes",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = FinoraSpacing.medium)
            )
        }
        
        item {
            FinoraCard {
                Column(
                    modifier = Modifier.padding(FinoraSpacing.medium),
                    verticalArrangement = Arrangement.spacedBy(FinoraSpacing.small)
                ) {
                    SwitchSetting(
                        title = "Alertas de Padrões Anormais",
                        subtitle = "Seja notificado sobre picos de gastos e padrões incomuns",
                        checked = config.insightAlertsEnabled,
                        onCheckedChange = onInsightAlertsChange
                    )
                    
                    if (config.insightAlertsEnabled) {
                        Divider(modifier = Modifier.padding(vertical = FinoraSpacing.small))
                        
                        Text(
                            text = "Sensibilidade: ${when {
                                config.spendingSpikeSensitivity <= 1.3 -> "Alta"
                                config.spendingSpikeSensitivity <= 1.7 -> "Média"
                                else -> "Baixa"
                            }}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        
                        Slider(
                            value = config.spendingSpikeSensitivity.toFloat(),
                            onValueChange = { onSpendingSensitivityChange(it.toDouble()) },
                            valueRange = 1.2f..2.0f,
                            steps = 7
                        )
                    }
                }
            }
        }
        
        // Espaço no final
        item {
            Spacer(Modifier.height(FinoraSpacing.huge))
        }
    }
}

@Composable
private fun SwitchSetting(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}
