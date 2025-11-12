package com.finora.features.budgets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.finora.domain.model.BudgetPeriod
import com.finora.domain.model.ExpenseCategory
import com.finora.ui.theme.FinoraSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBudgetScreen(
    onNavigateBack: () -> Unit,
    viewModel: AddBudgetViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Novo Orçamento") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Voltar")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.saveBudget(onSuccess = onNavigateBack)
                        },
                        enabled = !uiState.isSaving && uiState.limitAmount.isNotBlank()
                    ) {
                        if (uiState.isSaving) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(Icons.Default.Check, "Salvar")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(FinoraSpacing.medium),
            verticalArrangement = Arrangement.spacedBy(FinoraSpacing.large)
        ) {
            // Seletor de Categoria
            CategoryDropdown(
                selectedCategory = uiState.category,
                onCategorySelected = viewModel::onCategoryChanged,
                modifier = Modifier.fillMaxWidth()
            )
            
            // Valor Limite
            OutlinedTextField(
                value = uiState.limitAmount,
                onValueChange = viewModel::onLimitAmountChanged,
                label = { Text("Valor Limite") },
                placeholder = { Text("0.00") },
                prefix = { Text("R$ ") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isSaving,
                singleLine = true
            )
            
            // Período
            Text(
                text = "Período",
                style = MaterialTheme.typography.labelLarge
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(FinoraSpacing.medium)
            ) {
                FilterChip(
                    selected = uiState.period == BudgetPeriod.MONTHLY,
                    onClick = { viewModel.onPeriodChanged(BudgetPeriod.MONTHLY) },
                    label = { Text("Mensal") },
                    modifier = Modifier.weight(1f),
                    enabled = !uiState.isSaving
                )
                
                FilterChip(
                    selected = uiState.period == BudgetPeriod.ANNUAL,
                    onClick = { viewModel.onPeriodChanged(BudgetPeriod.ANNUAL) },
                    label = { Text("Anual") },
                    modifier = Modifier.weight(1f),
                    enabled = !uiState.isSaving
                )
            }
            
            // Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Text(
                    text = "💡 O orçamento será válido a partir de hoje por ${if (uiState.period == BudgetPeriod.MONTHLY) "30 dias" else "1 ano"}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(FinoraSpacing.medium),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryDropdown(
    selectedCategory: ExpenseCategory,
    onCategorySelected: (ExpenseCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedCategory.displayName,
            onValueChange = {},
            readOnly = true,
            label = { Text("Categoria") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
        )
        
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            ExpenseCategory.entries.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category.displayName) },
                    onClick = {
                        onCategorySelected(category)
                        expanded = false
                    }
                )
            }
        }
    }
}
