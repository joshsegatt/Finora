package com.finora.expenses.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    val darkMode by viewModel.darkMode.collectAsState()
    val currency by viewModel.currency.collectAsState()
    val notificationsEnabled by viewModel.notificationsEnabled.collectAsState()
    var showCurrencyDialog by remember { mutableStateOf(false) }
    var showExportSuccess by remember { mutableStateOf(false) }
    var showImportSuccess by remember { mutableStateOf<Int?>(null) }
    
    Scaffold(topBar = { TopAppBar(title = { Text("Settings") }) }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
                Text("Appearance", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                SettingsItem(Icons.Default.DarkMode, "Dark Mode", if (darkMode) "Enabled" else "Disabled") {
                    Switch(darkMode, viewModel::setDarkMode)
                }
            }
            item {
                Spacer(Modifier.height(16.dp))
                Text("General", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                SettingsItem(Icons.Default.AttachMoney, "Default Currency", currency, onClick = { showCurrencyDialog = true })
            }
            item { SettingsItem(Icons.Default.Notifications, "Notifications", if (notificationsEnabled) "Enabled" else "Disabled") { Switch(notificationsEnabled, viewModel::setNotifications) } }
            item {
                Spacer(Modifier.height(16.dp))
                Text("Data", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                SettingsItem(Icons.Default.Upload, "Export to CSV", "Save all expenses", onClick = { 
                    viewModel.exportToCsv()
                    showExportSuccess = true
                })
            }
            item { SettingsItem(Icons.Default.Download, "Import from CSV", "Load expenses from file", onClick = { viewModel.importFromCsv() }) }
            item {
                Spacer(Modifier.height(16.dp))
                Text("About", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                SettingsItem(Icons.Default.Info, "Version", "1.0.0")
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
            text = { LazyColumn { items(currencies.size) { i -> Row(Modifier.fillMaxWidth().clickable { viewModel.setCurrency(currencies[i]); showCurrencyDialog = false }.padding(12.dp)) { RadioButton(currencies[i] == currency, null); Spacer(Modifier.width(12.dp)); Text(currencies[i]) } } } },
            confirmButton = { TextButton({ showCurrencyDialog = false }) { Text("Cancel") } }
        )
    }
}

@Composable
fun SettingsItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, subtitle: String? = null, onClick: (() -> Unit)? = null, trailing: @Composable (() -> Unit)? = null) {
    Card(Modifier.fillMaxWidth().then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)) {
        Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.bodyLarge)
                if (subtitle != null) Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            trailing?.invoke()
        }
    }
}
