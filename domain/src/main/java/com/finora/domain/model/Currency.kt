package com.finora.domain.model

import java.util.Locale

/**
 * Supported currencies in Finora.
 * Auto-detects based on user's locale.
 */
enum class Currency(
    val code: String,
    val symbol: String,
    val displayName: String,
    val defaultForCountries: List<String> = emptyList()
) {
    GBP(
        code = "GBP",
        symbol = "£",
        displayName = "British Pound",
        defaultForCountries = listOf("GB", "UK")
    ),
    EUR(
        code = "EUR",
        symbol = "€",
        displayName = "Euro",
        defaultForCountries = listOf(
            "AT", "BE", "CY", "EE", "FI", "FR", "DE", "GR", "IE", "IT",
            "LV", "LT", "LU", "MT", "NL", "PT", "SK", "SI", "ES"
        )
    ),
    USD(
        code = "USD",
        symbol = "$",
        displayName = "US Dollar",
        defaultForCountries = listOf("US")
    ),
    BRL(
        code = "BRL",
        symbol = "R$",
        displayName = "Brazilian Real",
        defaultForCountries = listOf("BR")
    ),
    CHF(
        code = "CHF",
        symbol = "CHF",
        displayName = "Swiss Franc",
        defaultForCountries = listOf("CH")
    ),
    JPY(
        code = "JPY",
        symbol = "¥",
        displayName = "Japanese Yen",
        defaultForCountries = listOf("JP")
    ),
    CAD(
        code = "CAD",
        symbol = "C$",
        displayName = "Canadian Dollar",
        defaultForCountries = listOf("CA")
    ),
    AUD(
        code = "AUD",
        symbol = "A$",
        displayName = "Australian Dollar",
        defaultForCountries = listOf("AU")
    ),
    INR(
        code = "INR",
        symbol = "₹",
        displayName = "Indian Rupee",
        defaultForCountries = listOf("IN")
    );

    companion object {
        /**
         * Detects currency based on device locale.
         * Falls back to GBP for UK/Europe.
         */
        fun fromLocale(locale: Locale = Locale.getDefault()): Currency {
            val countryCode = locale.country.uppercase()
            
            return entries.find { currency ->
                currency.defaultForCountries.contains(countryCode)
            } ?: GBP // Default to GBP for Europe/UK
        }
        
        /**
         * Get currency by code (e.g., "GBP", "EUR").
         */
        fun fromCode(code: String): Currency? {
            return entries.find { it.code.equals(code, ignoreCase = true) }
        }
    }
    
    /**
     * Format amount with currency symbol.
     * Examples:
     * - GBP: £1,234.56
     * - EUR: €1.234,56
     * - USD: $1,234.56
     */
    fun format(amount: Double): String {
        val formatted = when (this) {
            EUR -> String.format(Locale.GERMANY, "%,.2f", amount)
            else -> String.format(Locale.UK, "%,.2f", amount)
        }
        
        return when (this) {
            BRL -> "R$ $formatted"
            CHF -> "$formatted CHF"
            else -> "$symbol$formatted"
        }
    }
}

/**
 * User's region for merchant detection.
 */
enum class Region(
    val code: String,
    val displayName: String,
    val countries: List<String>
) {
    UK(
        code = "UK",
        displayName = "United Kingdom",
        countries = listOf("GB", "UK")
    ),
    EUROPE(
        code = "EU",
        displayName = "Europe",
        countries = listOf(
            "AT", "BE", "CY", "CZ", "DK", "EE", "FI", "FR", "DE", "GR",
            "HU", "IE", "IT", "LV", "LT", "LU", "MT", "NL", "PL", "PT",
            "RO", "SK", "SI", "ES", "SE", "BG", "HR"
        )
    ),
    NORTH_AMERICA(
        code = "NA",
        displayName = "North America",
        countries = listOf("US", "CA", "MX")
    ),
    SOUTH_AMERICA(
        code = "SA",
        displayName = "South America",
        countries = listOf("BR", "AR", "CL", "CO", "PE", "VE")
    ),
    ASIA(
        code = "AS",
        displayName = "Asia",
        countries = listOf("JP", "CN", "KR", "IN", "SG", "TH", "MY", "ID")
    ),
    OCEANIA(
        code = "OC",
        displayName = "Oceania",
        countries = listOf("AU", "NZ")
    );

    companion object {
        /**
         * Detects region based on device locale.
         * Falls back to UK/Europe.
         */
        fun fromLocale(locale: Locale = Locale.getDefault()): Region {
            val countryCode = locale.country.uppercase()
            
            return entries.find { region ->
                region.countries.contains(countryCode)
            } ?: UK // Default to UK
        }
        
        fun fromCode(code: String): Region? {
            return entries.find { it.code.equals(code, ignoreCase = true) }
        }
    }
}
