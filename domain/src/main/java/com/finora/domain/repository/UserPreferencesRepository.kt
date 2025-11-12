package com.finora.domain.repository

import com.finora.domain.model.Currency
import com.finora.domain.model.Region
import com.finora.domain.model.UserPreferences
import kotlinx.coroutines.flow.Flow

/**
 * Repository for managing user preferences (currency, region, language).
 */
interface UserPreferencesRepository {
    
    /**
     * Observe user preferences as Flow.
     */
    fun getUserPreferences(): Flow<UserPreferences>
    
    /**
     * Update currency preference.
     */
    suspend fun setCurrency(currency: Currency)
    
    /**
     * Update region preference.
     */
    suspend fun setRegion(region: Region)
    
    /**
     * Update language preference.
     */
    suspend fun setLanguage(languageCode: String)
    
    /**
     * Reset to defaults (auto-detect from locale).
     */
    suspend fun resetToDefaults()
}
