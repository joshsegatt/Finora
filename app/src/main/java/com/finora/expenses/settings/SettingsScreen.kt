package com.finora.expenses.settings

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.finora.ui.theme.components.Elite3DTitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    val darkMode by viewModel.darkMode.collectAsState()
    val currency by viewModel.currency.collectAsState()
    val notificationsEnabled by viewModel.notificationsEnabled.collectAsState()
    var showCurrencyDialog by remember { mutableStateOf(false) }
    var showExportSuccess by remember { mutableStateOf(false) }
    var showImportSuccess by remember { mutableStateOf<Int?>(null) }
    
    val context = LocalContext.current
    
    // File Picker Launcher para Import CSV
    val importCsvLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.importFromCsv(context, it) { count ->
                showImportSuccess = count
            }
        }
    }
    
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Título 3D Elite
            Elite3DTitle(
                text = "Settings",
                icon = Icons.Default.Settings,
                modifier = Modifier.padding(top = 8.dp)
            )
            
            // Content
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Appearance Section
                SectionHeader(title = "Appearance", icon = Icons.Default.Palette)
                SettingsItem3D(
                    icon = Icons.Default.DarkMode,
                    title = "Dark Mode",
                    subtitle = if (darkMode) "Enabled" else "Disabled",
                    iconBackgroundColor = MaterialTheme.colorScheme.primary,
                    trailing = {
                        Switch(
                        checked = darkMode,
                        onCheckedChange = { viewModel.setDarkMode(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.primary,
                            checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                            uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                            uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    )
                }
            )
            
            // General Section
            SectionHeader(title = "General", icon = Icons.Default.Settings)
            SettingsItem3D(
                icon = Icons.Default.AttachMoney,
                title = "Currency",
                subtitle = currency,
                iconBackgroundColor = Color(0xFF4CAF50),
                onClick = { showCurrencyDialog = true }
            )
            
            SettingsItem3D(
                icon = Icons.Default.Notifications,
                title = "Notifications",
                subtitle = if (notificationsEnabled) "Enabled" else "Disabled",
                iconBackgroundColor = Color(0xFFFF9800),
                trailing = {
                    Switch(
                        checked = notificationsEnabled,
                        onCheckedChange = { viewModel.setNotifications(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.primary,
                            checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                            uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                            uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    )
                }
            )
            
            // Data Section
            SectionHeader(title = "Data", icon = Icons.Default.Storage)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SettingsItem3D(
                    icon = Icons.Default.Upload,
                    title = "Export",
                    subtitle = "CSV",
                    iconBackgroundColor = Color(0xFF2196F3),
                    onClick = { 
                        viewModel.exportToCsv()
                        showExportSuccess = true
                    },
                    modifier = Modifier.weight(1f)
                )
                
                SettingsItem3D(
                    icon = Icons.Default.Download,
                    title = "Import",
                    subtitle = "CSV",
                    iconBackgroundColor = Color(0xFF9C27B0),
                    onClick = { importCsvLauncher.launch("text/*") },
                    modifier = Modifier.weight(1f)
                )
            }
            
            // About Section
            SectionHeader(title = "About", icon = Icons.Default.Info)
            SettingsItem3D(
                icon = Icons.Default.Info,
                title = "Version",
                subtitle = "1.0.0",
                iconBackgroundColor = Color(0xFF607D8B)
            )
            }
        }
    }
    
    if (showExportSuccess) {
        AlertDialog(
            onDismissRequest = { showExportSuccess = false },
            title = { Text("Export Successful") },
            text = { Text("Expenses exported to Downloads/finora_expenses.csv") },
            confirmButton = { TextButton(onClick = { 
                showExportSuccess = false
                viewModel.shareCsv()
            }) { Text("Share") } },
            dismissButton = { TextButton(onClick = { showExportSuccess = false }) { Text("OK") } }
        )
    }
    
    showImportSuccess?.let { count ->
        AlertDialog(
            onDismissRequest = { showImportSuccess = null },
            title = { Text("Import Successful") },
            text = { Text("Imported $count expenses") },
            confirmButton = { TextButton(onClick = { showImportSuccess = null }) { Text("OK") } }
        )
    }
    
    if (showCurrencyDialog) {
        val currencies = listOf("USD", "EUR", "GBP", "JPY", "CAD", "AUD", "CHF", "CNY", "INR")
        AlertDialog(
            onDismissRequest = { showCurrencyDialog = false },
            title = { Text("Select Currency") },
            text = { 
                LazyColumn { 
                    items(currencies.size) { i -> 
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { 
                                    viewModel.setCurrency(currencies[i])
                                    showCurrencyDialog = false
                                }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) { 
                            RadioButton(currencies[i] == currency, null)
                            Spacer(Modifier.width(12.dp))
                            Text(currencies[i])
                        } 
                    } 
                } 
            },
            confirmButton = { TextButton({ showCurrencyDialog = false }) { Text("Cancel") } }
        )
    }
}

@Composable
fun SectionHeader(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun SettingsItem3D(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    iconBackgroundColor: Color,
    onClick: (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isPressed) 0.97f else 1f)
    val elevation by animateFloatAsState(if (isPressed) 2f else 6f)
    
    Card(
        modifier = modifier
            .scale(scale)
            .shadow(
                elevation = elevation.dp,
                shape = RoundedCornerShape(12.dp),
                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
            )
            .then(
                if (onClick != null) {
                    Modifier.clickable {
                        isPressed = true
                        onClick()
                    }
                } else {
                    Modifier
                }
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        )
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon with 3D effect
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .shadow(
                            elevation = 3.dp,
                            shape = CircleShape,
                            spotColor = iconBackgroundColor.copy(alpha = 0.5f)
                        )
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    iconBackgroundColor.copy(alpha = 0.9f),
                                    iconBackgroundColor.copy(alpha = 0.7f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                
                Spacer(Modifier.width(12.dp))
                
                Column(Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                trailing?.invoke()
            }
        }
    }
    
    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(100)
            isPressed = false
        }
    }
}
