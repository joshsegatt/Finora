package com.finora.core

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

data class SnackbarMessage(
    val message: String,
    val actionLabel: String? = null,
    val onAction: (() -> Unit)? = null
)

@Singleton
class SnackbarController @Inject constructor() {
    
    private val _messages = MutableSharedFlow<SnackbarMessage>(replay = 0)
    val messages = _messages.asSharedFlow()
    
    fun showError(message: String, actionLabel: String? = null, onAction: (() -> Unit)? = null) {
        GlobalScope.launch {
            _messages.emit(SnackbarMessage(message, actionLabel, onAction))
        }
    }
}
