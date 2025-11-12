package com.finora.domain.model

import java.util.Date

/**
 * User preferences for currency, region, and language.
 * Persisted in DataStore.
 */
data class UserPreferences(
    val currency: Currency = Currency.GBP,
    val region: Region = Region.UK,
    val languageCode: String = "en", // ISO 639-1 (en, pt, es, fr, de)
    val dateFormat: DateFormat = DateFormat.UK,
    val lastUpdated: Date = Date()
)

/**
 * Date format preferences.
 */
enum class DateFormat(val pattern: String, val example: String) {
    UK("dd/MM/yyyy", "25/12/2024"),
    US("MM/dd/yyyy", "12/25/2024"),
    ISO("yyyy-MM-dd", "2024-12-25");
    
    companion object {
        fun fromRegion(region: Region): DateFormat {
            return when (region) {
                Region.UK, Region.EUROPE, Region.OCEANIA -> UK
                Region.NORTH_AMERICA -> US
                else -> UK
            }
        }
    }
}
