package com.finora.data.ocr

import com.finora.core.CurrencyFormatter
import com.finora.core.DateFormatter
import com.finora.core.Logger
import com.finora.domain.model.ExpenseCategory
import com.finora.domain.model.ReceiptData
import java.util.Date

object ReceiptParser {
    
    private val amountPatterns = listOf(
        Regex("""(?:TOTAL|Total|AMOUNT|Amount|SUBTOTAL|Subtotal)[:\s]*\$?(\d+[.,]\d{2})"""),
        Regex("""\$\s*(\d+[.,]\d{2})"""),
        Regex("""(\d+[.,]\d{2})\s*(?:USD|EUR|GBP|BRL)"""),
        Regex("""(?:^|\s)(\d+[.,]\d{2})(?:\s|$)""")
    )
    
    private val datePatterns = listOf(
        Regex("""(\d{1,2})[/-](\d{1,2})[/-](\d{2,4})"""),
        Regex("""(\d{2,4})[/-](\d{1,2})[/-](\d{1,2})""")
    )
    
    private val merchantPatterns = listOf(
        Regex("""^([A-Z][A-Za-z\s&'-]{2,30})(?:\s|$)""", RegexOption.MULTILINE)
    )
    
    fun parse(rawText: String): ReceiptData {
        Logger.d("Parsing receipt text: ${rawText.take(100)}...")
        
        val amount = extractAmount(rawText)
        val date = extractDate(rawText)
        val merchant = extractMerchant(rawText)
        val items = extractItems(rawText)
        val category = ExpenseCategory.inferFromText(rawText)
        
        val confidence = calculateConfidence(amount, date, merchant)
        
        return ReceiptData(
            rawText = rawText,
            amount = amount,
            date = date,
            merchant = merchant,
            items = items,
            inferredCategory = category,
            confidence = confidence
        )
    }
    
    private fun extractAmount(text: String): Double? {
        for (pattern in amountPatterns) {
            val match = pattern.findAll(text).lastOrNull()
            if (match != null) {
                val amountStr = match.groupValues[1]
                val amount = CurrencyFormatter.parse(amountStr)
                if (amount != null && amount > 0) {
                    Logger.d("Extracted amount: $amount")
                    return amount
                }
            }
        }
        
        Logger.w("No valid amount found in receipt")
        return null
    }
    
    private fun extractDate(text: String): Date? {
        for (pattern in datePatterns) {
            val match = pattern.find(text)
            if (match != null) {
                val dateStr = match.value
                val parsed = DateFormatter.parseDate(dateStr)
                if (parsed != null) {
                    Logger.d("Extracted date: $parsed")
                    return parsed
                }
            }
        }
        
        Logger.w("No valid date found in receipt, using current date")
        return Date()
    }
    
    private fun extractMerchant(text: String): String? {
        val lines = text.lines().filter { it.isNotBlank() }
        if (lines.isEmpty()) return null
        
        for (pattern in merchantPatterns) {
            val match = pattern.find(text)
            if (match != null) {
                val merchant = match.groupValues[1].trim()
                if (merchant.length >= 3) {
                    Logger.d("Extracted merchant: $merchant")
                    return merchant
                }
            }
        }
        
        val firstLine = lines.first().trim()
        if (firstLine.length in 3..50) {
            Logger.d("Using first line as merchant: $firstLine")
            return firstLine
        }
        
        return null
    }
    
    private fun extractItems(text: String): List<String> {
        return text.lines()
            .filter { line ->
                line.isNotBlank() &&
                line.length > 3 &&
                line.any { it.isLetter() } &&
                !line.matches(Regex("""^\d+$"""))
            }
            .take(10)
            .map { it.trim() }
    }
    
    private fun calculateConfidence(amount: Double?, date: Date?, merchant: String?): Float {
        var confidence = 0f
        if (amount != null && amount > 0) confidence += 0.5f
        if (date != null) confidence += 0.2f
        if (merchant != null && merchant.isNotBlank()) confidence += 0.3f
        return confidence
    }
}
