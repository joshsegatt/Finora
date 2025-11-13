package com.finora.expenses.settings

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.finora.core.Logger
import com.finora.core.Result
import com.finora.core.datastore.PreferencesManager
import com.finora.domain.usecase.ExportExpensesToCsvUseCase
import com.finora.domain.usecase.ImportExpensesFromCsvUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val app: Application,
    private val preferencesManager: PreferencesManager,
    private val exportExpensesToCsvUseCase: ExportExpensesToCsvUseCase,
    private val importExpensesFromCsvUseCase: ImportExpensesFromCsvUseCase
) : AndroidViewModel(app) {
    val darkMode = preferencesManager.darkMode.stateIn(viewModelScope, SharingStarted.Eagerly, false)
    val currency = preferencesManager.currency.stateIn(viewModelScope, SharingStarted.Eagerly, "USD")
    val notificationsEnabled = preferencesManager.notificationsEnabled.stateIn(viewModelScope, SharingStarted.Eagerly, true)
    
    private var lastExportedFile: File? = null
    
    fun setDarkMode(enabled: Boolean) { viewModelScope.launch { preferencesManager.setDarkMode(enabled) } }
    fun setCurrency(curr: String) { viewModelScope.launch { preferencesManager.setCurrency(curr) } }
    fun setNotifications(enabled: Boolean) { viewModelScope.launch { preferencesManager.setNotificationsEnabled(enabled) } }
    
    fun exportToCsv() {
        viewModelScope.launch {
            val downloadsDir = File(app.getExternalFilesDir(null), "exports")
            downloadsDir.mkdirs()
            val file = File(downloadsDir, "finora_expenses_${System.currentTimeMillis()}.csv")
            
            when (val result = exportExpensesToCsvUseCase(ExportExpensesToCsvUseCase.Params(file))) {
                is Result.Success -> {
                    lastExportedFile = result.data
                    Logger.d("CSV exported to: ${result.data.absolutePath}")
                }
                is Result.Failure -> Logger.e("Export failed: ${result.error.message}")
            }
        }
    }
    
    fun importFromCsv(context: Context, uri: Uri, onSuccess: (Int) -> Unit) {
        viewModelScope.launch {
            try {
                // Copy URI content to temporary file
                val tempFile = File(context.cacheDir, "temp_import_${System.currentTimeMillis()}.csv")
                context.contentResolver.openInputStream(uri)?.use { input ->
                    tempFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                
                // Import from temporary file
                when (val result = importExpensesFromCsvUseCase(ImportExpensesFromCsvUseCase.Params(tempFile))) {
                    is Result.Success -> {
                        Logger.d("Imported ${result.data} expenses")
                        onSuccess(result.data)
                        tempFile.delete()
                    }
                    is Result.Failure -> {
                        Logger.e("Import failed: ${result.error.message}")
                        tempFile.delete()
                    }
                }
            } catch (e: Exception) {
                Logger.e("Import error: ${e.message}")
            }
        }
    }
    
    fun shareCsv() {
        lastExportedFile?.let { file ->
            try {
                val uri = FileProvider.getUriForFile(
                    app,
                    "${app.packageName}.fileprovider",
                    file
                )
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/csv"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                app.startActivity(Intent.createChooser(intent, "Share CSV").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            } catch (e: Exception) {
                Logger.e("Share failed: ${e.message}")
            }
        }
    }
}
