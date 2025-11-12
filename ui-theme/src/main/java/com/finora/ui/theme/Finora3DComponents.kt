package com.finora.ui.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Chip 3D Sofisticado para Filtros (All, Food, Dining, etc)
 * - Adaptável ao modo claro/escuro
 * - Animações suaves
 * - Elevação 3D com sombras
 */
@Composable
fun Finora3DFilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    // Animações suaves
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = tween(100),
        label = "chipScale"
    )
    
    val elevation by animateFloatAsState(
        targetValue = when {
            isPressed -> 1f
            selected -> 6f
            else -> 3f
        },
        animationSpec = tween(150),
        label = "chipElevation"
    )
    
    // Cores adaptativas (light/dark mode) - ALTO CONTRASTE
    val backgroundColor by animateColorAsState(
        targetValue = when {
            selected -> MaterialTheme.colorScheme.primary // Deep Blue quando selecionado
            else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f) // Mais transparente quando não selecionado
        },
        animationSpec = tween(200),
        label = "chipBackground"
    )
    
    val contentColor by animateColorAsState(
        targetValue = when {
            selected -> MaterialTheme.colorScheme.onPrimary // Branco/Claro no texto
            else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f) // Mais fraco quando não selecionado
        },
        animationSpec = tween(200),
        label = "chipContent"
    )
    
    val borderColor = when {
        selected -> Color.Transparent
        else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.5f) // Borda mais visível
    }
    
    // Gradiente sutil 3D - MAIS CONTRASTE
    val gradientColors = if (selected) {
        listOf(
            backgroundColor, // Primary cheio
            backgroundColor.copy(alpha = 0.85f) // Gradiente mais pronunciado
        )
    } else {
        listOf(
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f), // Bem transparente
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    }

    Box(
        modifier = modifier
            .scale(scale)
            .shadow(
                elevation = elevation.dp,
                shape = RoundedCornerShape(50),
                ambientColor = if (selected) {
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                } else {
                    Color.Black.copy(alpha = 0.1f)
                },
                spotColor = if (selected) {
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
                } else {
                    Color.Black.copy(alpha = 0.15f)
                }
            )
            .background(
                brush = Brush.verticalGradient(gradientColors),
                shape = RoundedCornerShape(50)
            )
            .border(
                width = if (selected) 0.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(50)
            )
            .clip(RoundedCornerShape(50))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 20.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                letterSpacing = 0.5.sp
            ),
            color = contentColor
        )
    }
}

/**
 * Tab 3D Sofisticado para Períodos (Daily, Weekly, Monthly, Yearly)
 * - Alinhamento perfeito sem indicador extra
 * - Alto contraste light/dark mode
 * - Bordas sutis + sombras 3D
 */
@Composable
fun Finora3DPeriodTab(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    // Animações
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = tween(100),
        label = "tabScale"
    )
    
    val elevation by animateFloatAsState(
        targetValue = when {
            isPressed -> 1f
            selected -> 8f // Maior elevação quando selecionado
            else -> 1f // Quase plano quando não selecionado
        },
        animationSpec = tween(150),
        label = "tabElevation"
    )
    
    // Cores adaptativas com CONTRASTE MÁXIMO
    val backgroundColor by animateColorAsState(
        targetValue = when {
            selected -> MaterialTheme.colorScheme.primary // Deep Blue forte
            else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f) // Muito transparente
        },
        animationSpec = tween(200),
        label = "tabBackground"
    )
    
    val contentColor by animateColorAsState(
        targetValue = when {
            selected -> MaterialTheme.colorScheme.onPrimary // Branco/Gold
            else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f) // Bem apagado
        },
        animationSpec = tween(200),
        label = "tabContent"
    )
    
    // Borda sutil quando não selecionado
    val borderColor = when {
        selected -> Color.Transparent
        else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.5f) // Borda mais visível
    }
    
    // Gradiente 3D sofisticado - MAIS CONTRASTE
    val gradientColors = if (selected) {
        listOf(
            backgroundColor, // Primary cheio
            backgroundColor.copy(alpha = 0.85f) // Gradiente pronunciado
        )
    } else {
        listOf(
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f), // Bem transparente
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
        )
    }

    Box(
        modifier = modifier
            .scale(scale)
            .shadow(
                elevation = elevation.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = if (selected) {
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                } else {
                    Color.Black.copy(alpha = 0.08f)
                },
                spotColor = if (selected) {
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                } else {
                    Color.Black.copy(alpha = 0.12f)
                }
            )
            .background(
                brush = Brush.verticalGradient(gradientColors),
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = if (selected) 0.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 16.dp, vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold,
                letterSpacing = 0.4.sp,
                fontSize = 13.sp
            ),
            color = contentColor,
            maxLines = 1
        )
    }
}

/**
 * BarChart Animado 3D para Reports
 * - Mostra gastos ao longo do período (dias/semanas/meses)
 * - Animação de crescimento suave
 * - Gradientes verticais 3D
 * - Labels de valores
 */
@Composable
fun AnimatedBarChart(
    data: List<Pair<String, Double>>, // Label (ex: "Mon", "Week 1") + Valor
    modifier: Modifier = Modifier,
    maxBars: Int = 7, // Máximo de barras visíveis
    animate: Boolean = true
) {
    // Pegar apenas os últimos N itens
    val displayData = data.takeLast(maxBars)
    val maxValue = displayData.maxOfOrNull { it.second } ?: 1.0
    
    // Animação de altura das barras
    val animatedHeights = displayData.map { (_, value) ->
        val targetHeight = if (maxValue > 0) (value / maxValue).toFloat() else 0f
        if (animate) {
            animateFloatAsState(
                targetValue = targetHeight,
                animationSpec = tween(durationMillis = 600, easing = androidx.compose.animation.core.FastOutSlowInEasing),
                label = "barHeight"
            ).value
        } else {
            targetHeight
        }
    }
    
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Spending Trend",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            if (displayData.isEmpty()) {
                // Mensagem quando não há dados
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No data for this period",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                // Gráfico de barras
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    displayData.forEachIndexed { index, (label, value) ->
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Valor acima da barra (se houver espaço)
                        if (animatedHeights[index] > 0.3f) {
                            Text(
                                text = if (value >= 1000) {
                                    "${(value / 1000).toInt()}k"
                                } else {
                                    value.toInt().toString()
                                },
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium
                                ),
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                        
                        // Barra 3D com gradiente
                        Box(
                            modifier = Modifier
                                .width(32.dp)
                                .fillMaxHeight(animatedHeights[index])
                                .shadow(
                                    elevation = 4.dp,
                                    shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                                )
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                                        )
                                    ),
                                    shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                                )
                        )
                        
                        // Label abaixo da barra
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                    } // Fecha Column
                } // Fecha forEachIndexed
                } // Fecha Row
            } // Fecha else
        } // Fecha Column principal
    } // Fecha Card
} // Fecha AnimatedBarChart
