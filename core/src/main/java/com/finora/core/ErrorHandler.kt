package com.finora.core

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorHandler @Inject constructor() {
    
    private val _errorEvents = MutableStateFlow<ErrorEvent?>(null)
    val errorEvents = _errorEvents.asStateFlow()
    
    fun handleError(error: AppError, retryAction: (() -> Unit)? = null) {
        Logger.e("Error handled: ${error.message}")
        _errorEvents.value = ErrorEvent(
            message = error.toUserMessage(),
            canRetry = retryAction != null,
            retryAction = retryAction
        )
    }
    
    fun clearError() {
        _errorEvents.value = null
    }
}

data class ErrorEvent(
    val message: String,
    val canRetry: Boolean = false,
    val retryAction: (() -> Unit)? = null
)

fun AppError.toUserMessage(): String = when (this) {
    is AppError.NetworkError -> "No internet connection"
    is AppError.DatabaseError -> "Database error occurred"
    is AppError.ValidationError -> message
    is AppError.OcrError -> "Failed to scan receipt"
    is AppError.FileError -> "File operation failed"
    is AppError.ParseError -> "Failed to parse data"
    is AppError.UnknownError -> "Something went wrong"
}
