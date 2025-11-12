package com.finora.core

object InputValidator {
    
    fun validateAmount(amount: String): ValidationResult {
        if (amount.isBlank()) return ValidationResult.Error("Amount is required")
        val parsed = amount.toDoubleOrNull()
        if (parsed == null) return ValidationResult.Error("Invalid amount")
        if (parsed <= 0) return ValidationResult.Error("Amount must be positive")
        if (parsed > 1_000_000) return ValidationResult.Error("Amount too large")
        return ValidationResult.Success
    }
    
    fun validateDescription(desc: String): ValidationResult {
        if (desc.isBlank()) return ValidationResult.Error("Description is required")
        if (desc.length < 2) return ValidationResult.Error("Description too short")
        if (desc.length > 200) return ValidationResult.Error("Description too long")
        return ValidationResult.Success
    }
    
    fun validateMerchant(merchant: String?): ValidationResult {
        if (merchant != null && merchant.length > 100) return ValidationResult.Error("Merchant name too long")
        return ValidationResult.Success
    }
}

sealed class ValidationResult {
    object Success : ValidationResult()
    data class Error(val message: String) : ValidationResult()
}
