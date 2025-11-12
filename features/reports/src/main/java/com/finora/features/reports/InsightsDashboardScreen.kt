package com.finora.features.reports

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.finora.core.CurrencyFormatter
import com.finora.domain.model.CategorySpending
import com.finora.domain.model.MonthlyInsight
import com.finora.domain.model.MonthlyPrediction
import com.finora.domain.model.SpendingTrend
import com.finora.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsightsDashboardScreen(
    viewModel: InsightsDashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Insights") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
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
                
                uiState.monthlyInsight == null -> {
                    EmptyInsightsState(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                
                else -> {
                    InsightsDashboardContent(
                        insight = uiState.monthlyInsight!!,
                        prediction = uiState.prediction
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
private fun InsightsDashboardContent(
    insight: MonthlyInsight,
    prediction: MonthlyPrediction?
) {
    LazyColumn(
        contentPadding = PaddingValues(FinoraSpacing.medium),
        verticalArrangement = Arrangement.spacedBy(FinoraSpacing.medium)
    ) {
        // Total do Mês
        item {
            MonthSummaryCard(insight)
        }
        
        // Comparação com Mês Anterior
        insight.comparisonWithLastMonth?.let { comparison ->
            item {
                ComparisonCard(comparison)
            }
        }
        
        // Previsão
        prediction?.let {
            item {
                PredictionCard(it)
            }
        }
        
        // Breakdown por Categoria
        item {
            Text(
                text = "Gastos por Categoria",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = FinoraSpacing.small)
            )
        }
        
        items(insight.categoryBreakdown) { categorySpending ->
            CategorySpendingItem(categorySpending)
        }
        
        // Estatísticas Extras
        item {
            ExtraStatsCard(insight)
        }
    }
}

@Composable
private fun MonthSummaryCard(insight: MonthlyInsight) {
    FinoraHighlightCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(FinoraSpacing.large),
            verticalArrangement = Arrangement.spacedBy(FinoraSpacing.small)
        ) {
            Text(
                text = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
                    .format(insight.month)
                    .replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Text(
                text = CurrencyFormatter.format(insight.totalSpent, "BRL"),
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = "Total gasto este mês",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ComparisonCard(comparison: com.finora.domain.model.SpendingComparison) {
    FinoraCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(FinoraSpacing.medium),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "vs. Mês Anterior",
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = CurrencyFormatter.format(comparison.previousAmount, "BRL"),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(FinoraSpacing.small)
            ) {
                Icon(
                    imageVector = when (comparison.trend) {
                        SpendingTrend.INCREASING -> Icons.Default.TrendingUp
                        SpendingTrend.DECREASING -> Icons.Default.TrendingDown
                        SpendingTrend.STABLE -> Icons.Default.TrendingUp
                    },
                    contentDescription = null,
                    tint = when (comparison.trend) {
                        SpendingTrend.INCREASING -> MaterialTheme.colorScheme.error
                        SpendingTrend.DECREASING -> MaterialTheme.colorScheme.primary
                        SpendingTrend.STABLE -> MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
                
                Text(
                    text = "${if (comparison.differenceAmount > 0) "+" else ""}${comparison.differencePercentage.toInt()}%",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = when (comparison.trend) {
                        SpendingTrend.INCREASING -> MaterialTheme.colorScheme.error
                        SpendingTrend.DECREASING -> MaterialTheme.colorScheme.primary
                        SpendingTrend.STABLE -> MaterialTheme.colorScheme.onSurface
                    }
                )
            }
        }
    }
}

@Composable
private fun PredictionCard(prediction: MonthlyPrediction) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(FinoraSpacing.medium),
            verticalArrangement = Arrangement.spacedBy(FinoraSpacing.small)
        ) {
            Text(
                text = "📊 Previsão Mensal",
                style = MaterialTheme.typography.titleMedium
            )
            
            Text(
                text = CurrencyFormatter.format(prediction.predictedAmount, "BRL"),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = "Baseado em ${prediction.basedOnMonths} meses • ${prediction.confidence.toInt()}% confiança",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            
            prediction.recommendation?.let {
                Spacer(Modifier.height(FinoraSpacing.small))
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

@Composable
private fun CategorySpendingItem(spending: CategorySpending) {
    FinoraCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(FinoraSpacing.medium),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = spending.category.displayName,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "${spending.count} despesas",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = CurrencyFormatter.format(spending.amount, "BRL"),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${spending.percentage.toInt()}%",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun ExtraStatsCard(insight: MonthlyInsight) {
    FinoraCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(FinoraSpacing.medium),
            verticalArrangement = Arrangement.spacedBy(FinoraSpacing.small)
        ) {
            Text(
                text = "Estatísticas Adicionais",
                style = MaterialTheme.typography.titleMedium
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Média Diária",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = CurrencyFormatter.format(insight.averageDailySpending, "BRL"),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Categoria Principal",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = insight.topCategory.displayName,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
            
            insight.mostExpensiveDay?.let { day ->
                Divider(modifier = Modifier.padding(vertical = FinoraSpacing.small))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Dia Mais Caro",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${SimpleDateFormat("dd/MM", Locale.getDefault()).format(day)} - ${CurrencyFormatter.format(insight.mostExpensiveDayAmount, "BRL")}",
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyInsightsState(modifier: Modifier = Modifier) {
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
            text = "Sem dados suficientes",
            style = MaterialTheme.typography.titleLarge
        )
        
        Text(
            text = "Adicione despesas para ver insights detalhados",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
