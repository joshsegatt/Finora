package com.finora.core

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Currency
import java.util.Date
import java.util.Locale

object DateFormatter {
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val dateTimeFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    private val monthYearFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
    
    fun formatDate(date: Date): String = dateFormat.format(date)
    fun formatDateTime(date: Date): String = dateTimeFormat.format(date)
    fun formatMonthYear(date: Date): String = monthYearFormat.format(date)
    
    fun parseDate(dateString: String): Date? {
        return try {
            dateFormat.parse(dateString)
        } catch (e: Exception) {
            Logger.e(e, "Failed to parse date: $dateString")
            null
        }
    }
}

/**
 * CurrencyFormatter - Legacy version using currency codes.
 * For dynamic currency support, use Currency.format() from domain layer.
 */
object CurrencyFormatter {
    fun format(amount: Double, currencyCode: String = "GBP"): String {
        return try {
            val format = NumberFormat.getCurrencyInstance(
                when (currencyCode) {
                    "EUR" -> Locale.GERMANY
                    "GBP" -> Locale.UK
                    else -> Locale.UK
                }
            )
            format.currency = Currency.getInstance(currencyCode)
            format.format(amount)
        } catch (e: Exception) {
            Logger.e(e, "Failed to format currency")
            "$currencyCode ${"%.2f".format(amount)}"
        }
    }
    
    fun parse(amountString: String): Double? {
        return try {
            amountString
                .replace("[^0-9.,]".toRegex(), "")
                .replace(",", ".")
                .toDoubleOrNull()
        } catch (e: Exception) {
            Logger.e(e, "Failed to parse amount: $amountString")
            null
        }
    }
}
