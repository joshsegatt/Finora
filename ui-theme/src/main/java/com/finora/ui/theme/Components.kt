package com.finora.ui.theme

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Espaçamentos Generosos - Respiração Visual
 */
object FinoraSpacing {
    val extraSmall = 4.dp
    val small = 8.dp
    val medium = 16.dp
    val large = 24.dp
    val extraLarge = 32.dp
    val huge = 48.dp
}

/**
 * Raio de Borda - Cantos Arredondados Elegantes
 */
object FinoraShapes {
    val extraSmall = RoundedCornerShape(4.dp)
    val small = RoundedCornerShape(8.dp)
    val medium = RoundedCornerShape(12.dp)
    val large = RoundedCornerShape(16.dp)
    val extraLarge = RoundedCornerShape(24.dp)
    val pill = RoundedCornerShape(50)
}

/**
 * Elevação - Sombras Discretas
 */
object FinoraElevation {
    val none = 0.dp
    val small = 2.dp
    val medium = 4.dp
    val large = 8.dp
    val extraLarge = 12.dp
}

/**
 * Botão Premium com Animação de Escala
 * Arredondado, sombra discreta, animação suave ao toque
 */
@Composable
fun FinoraPrimaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = tween(durationMillis = 100),
        label = "button_scale"
    )
    
    Button(
        onClick = onClick,
        modifier = modifier.scale(scale),
        enabled = enabled,
        interactionSource = interactionSource,
        shape = FinoraShapes.medium,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = FinoraElevation.small,
            pressedElevation = FinoraElevation.none,
            disabledElevation = FinoraElevation.none
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        contentPadding = PaddingValues(
            horizontal = FinoraSpacing.large,
            vertical = FinoraSpacing.medium
        ),
        content = content
    )
}

/**
 * Botão Secundário - Outlined com Animação
 */
@Composable
fun FinoraSecondaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = tween(durationMillis = 100),
        label = "button_scale"
    )
    
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.scale(scale),
        enabled = enabled,
        interactionSource = interactionSource,
        shape = FinoraShapes.medium,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        contentPadding = PaddingValues(
            horizontal = FinoraSpacing.large,
            vertical = FinoraSpacing.medium
        ),
        content = content
    )
}

/**
 * Card Elegante - Borda Fina e Sombra Suave
 */
@Composable
fun FinoraCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    elevation: Dp = FinoraElevation.small,
    border: BorderStroke? = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline),
    content: @Composable ColumnScope.() -> Unit
) {
    if (onClick != null) {
        Card(
            onClick = onClick,
            modifier = modifier,
            shape = FinoraShapes.medium,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = elevation),
            border = border,
            content = content
        )
    } else {
        Card(
            modifier = modifier,
            shape = FinoraShapes.medium,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = elevation),
            border = border,
            content = content
        )
    }
}

/**
 * Card Destacado - Para elementos importantes (totais, resumos)
 */
@Composable
fun FinoraHighlightCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        shape = FinoraShapes.large,
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = FinoraElevation.medium),
        border = null,
        content = content
    )
}

/**
 * Divisor Sutil - Separação Elegante
 */
@Composable
fun FinoraDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 0.5.dp,
    color: Color = MaterialTheme.colorScheme.outlineVariant
) {
    HorizontalDivider(
        modifier = modifier,
        thickness = thickness,
        color = color
    )
}

/**
 * Chip de Categoria - Pill Shape com Cor
 */
@Composable
fun FinoraCategoryChip(
    label: String,
    color: Color,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    if (onClick != null) {
        FilterChip(
            selected = selected,
            onClick = onClick,
            enabled = enabled,
            label = { Text(label) },
            modifier = modifier,
            shape = FinoraShapes.pill,
            colors = FilterChipDefaults.filterChipColors(
                containerColor = color.copy(alpha = 0.15f),
                labelColor = color,
                selectedContainerColor = color,
                selectedLabelColor = MaterialTheme.colorScheme.surface
            ),
            border = FilterChipDefaults.filterChipBorder(
                borderColor = color.copy(alpha = 0.3f),
                selectedBorderColor = color,
                borderWidth = 1.dp,
                enabled = enabled,
                selected = selected
            )
        )
    } else {
        Surface(
            modifier = modifier,
            shape = FinoraShapes.pill,
            color = color.copy(alpha = 0.15f),
            border = BorderStroke(1.dp, color.copy(alpha = 0.3f))
        ) {
            Text(
                text = label,
                modifier = Modifier.padding(
                    horizontal = FinoraSpacing.medium,
                    vertical = FinoraSpacing.small
                ),
                style = MaterialTheme.typography.labelMedium,
                color = color
            )
        }
    }
}
