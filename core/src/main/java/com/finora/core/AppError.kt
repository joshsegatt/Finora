package com.finora.core

sealed class AppError(open val message: String) {
    data class NetworkError(override val message: String = "Network error occurred") : AppError(message)
    data class DatabaseError(override val message: String = "Database error occurred") : AppError(message)
    data class ParseError(override val message: String = "Failed to parse data") : AppError(message)
    data class OcrError(override val message: String = "OCR processing failed") : AppError(message)
    data class FileError(override val message: String = "File operation failed") : AppError(message)
    data class ValidationError(override val message: String = "Validation failed") : AppError(message)
    data class UnknownError(override val message: String = "Unknown error occurred") : AppError(message)
    
    companion object {
        fun fromThrowable(throwable: Throwable): AppError {
            return when (throwable) {
                is java.io.IOException -> NetworkError(throwable.message ?: "Network error")
                is IllegalArgumentException -> ValidationError(throwable.message ?: "Validation error")
                else -> UnknownError(throwable.message ?: "Unknown error")
            }
        }
    }
}
