package com.finora.domain.model

enum class ExpenseCategory(val displayName: String) {
    FOOD("Food & Dining"),
    TRANSPORTATION("Transportation"),
    SHOPPING("Shopping"),
    ENTERTAINMENT("Entertainment"),
    UTILITIES("Utilities"),
    HEALTHCARE("Healthcare"),
    GROCERIES("Groceries"),
    EDUCATION("Education"),
    OTHER("Other");
    
    companion object {
        fun fromString(value: String): ExpenseCategory {
            return entries.find { 
                it.name.equals(value, ignoreCase = true) ||
                it.displayName.equals(value, ignoreCase = true)
            } ?: OTHER
        }
        
        fun inferFromText(text: String): ExpenseCategory {
            val lowerText = text.lowercase()
            return when {
                lowerText.contains("restaurant") || lowerText.contains("food") || 
                lowerText.contains("cafe") || lowerText.contains("coffee") -> FOOD
                
                lowerText.contains("uber") || lowerText.contains("taxi") || 
                lowerText.contains("gas") || lowerText.contains("fuel") -> TRANSPORTATION
                
                lowerText.contains("store") || lowerText.contains("shop") || 
                lowerText.contains("clothing") -> SHOPPING
                
                lowerText.contains("movie") || lowerText.contains("cinema") || 
                lowerText.contains("game") -> ENTERTAINMENT
                
                lowerText.contains("electric") || lowerText.contains("water") || 
                lowerText.contains("internet") || lowerText.contains("phone") -> UTILITIES
                
                lowerText.contains("pharmacy") || lowerText.contains("doctor") || 
                lowerText.contains("hospital") || lowerText.contains("clinic") -> HEALTHCARE
                
                lowerText.contains("supermarket") || lowerText.contains("grocery") || 
                lowerText.contains("market") -> GROCERIES
                
                lowerText.contains("school") || lowerText.contains("university") || 
                lowerText.contains("course") || lowerText.contains("book") -> EDUCATION
                
                else -> OTHER
            }
        }
    }
}
