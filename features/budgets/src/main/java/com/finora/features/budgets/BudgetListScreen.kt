package com.finora.features.budgets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.finora.domain.model.BudgetProgress
import com.finora.domain.model.BudgetStatus
import com.finora.ui.theme.FinoraCard
import com.finora.ui.theme.FinoraHighlightCard
import com.finora.ui.theme.FinoraPrimaryButton
import com.finora.ui.theme.FinoraSpacing
import com.finora.core.CurrencyFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetListScreen(
    onAddBudgetClick: () -> Unit,
    viewModel: BudgetListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Orçamentos") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddBudgetClick,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Novo Orçamento") }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading && uiState.budgetProgress.isEmpty() -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                
                uiState.budgetProgress.isEmpty() -> {
                    EmptyBudgetsState(
                        onAddClick = onAddBudgetClick,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                
                else -> {
                    BudgetList(
                        budgetProgress = uiState.budgetProgress,
                        onDeleteBudget = viewModel::deleteBudget
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
private fun BudgetList(
    budgetProgress: List<BudgetProgress>,
    onDeleteBudget: (Long) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(FinoraSpacing.medium),
        verticalArrangement = Arrangement.spacedBy(FinoraSpacing.medium)
    ) {
        items(budgetProgress, key = { it.budget.id }) { progress ->
            BudgetProgressCard(
                progress = progress,
                onDelete = { onDeleteBudget(progress.budget.id) }
            )
        }
    }
}

@Composable
private fun BudgetProgressCard(
    progress: BudgetProgress,
    onDelete: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    
    FinoraCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(FinoraSpacing.medium),
            verticalArrangement = Arrangement.spacedBy(FinoraSpacing.small)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = progress.budget.category.displayName,
                    style = MaterialTheme.typography.titleMedium
                )
                
                IconButton(onClick = { showDeleteDialog = true }) {
                    Icon(Icons.Default.Delete, "Deletar")
                }
            }
            
            Text(
                text = "${progress.budget.period.name} • ${CurrencyFormatter.format(progress.budget.limitAmount, "BRL")}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(Modifier.height(FinoraSpacing.small))
            
            // Barra de progresso
            LinearProgressIndicator(
                progress = { (progress.percentage / 100f).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = when (progress.status) {
                    BudgetStatus.OK -> MaterialTheme.colorScheme.primary
                    BudgetStatus.WARNING -> MaterialTheme.colorScheme.tertiary
                    BudgetStatus.EXCEEDED -> MaterialTheme.colorScheme.error
                }
            )
            
            Spacer(Modifier.height(FinoraSpacing.small))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Gasto",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = CurrencyFormatter.format(progress.currentSpent, "BRL"),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Restante",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = CurrencyFormatter.format(progress.remaining, "BRL"),
                        style = MaterialTheme.typography.bodyLarge,
                        color = when (progress.status) {
                            BudgetStatus.OK -> MaterialTheme.colorScheme.primary
                            BudgetStatus.WARNING -> MaterialTheme.colorScheme.tertiary
                            BudgetStatus.EXCEEDED -> MaterialTheme.colorScheme.error
                        }
                    )
                }
            }
            
            Text(
                text = "${progress.percentage.toInt()}% utilizado",
                style = MaterialTheme.typography.labelMedium,
                color = when (progress.status) {
                    BudgetStatus.OK -> MaterialTheme.colorScheme.onSurface
                    BudgetStatus.WARNING -> MaterialTheme.colorScheme.tertiary
                    BudgetStatus.EXCEEDED -> MaterialTheme.colorScheme.error
                }
            )
        }
    }
    
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Deletar Orçamento") },
            text = { Text("Tem certeza que deseja deletar este orçamento?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete()
                        showDeleteDialog = false
                    }
                ) {
                    Text("Deletar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun EmptyBudgetsState(
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(FinoraSpacing.huge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(FinoraSpacing.large)
    ) {
        Text(
            text = "📊",
            style = MaterialTheme.typography.displayLarge
        )
        
        Text(
            text = "Nenhum orçamento criado",
            style = MaterialTheme.typography.titleLarge
        )
        
        Text(
            text = "Crie orçamentos para controlar seus gastos por categoria",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(Modifier.height(FinoraSpacing.medium))
        
        FinoraPrimaryButton(
            onClick = onAddClick
        ) {
            Text("Criar Orçamento")
        }
    }
}
