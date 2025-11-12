package com.finora.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.finora.domain.model.Currency
import com.finora.domain.model.Region
import com.finora.domain.model.UserPreferences
import com.finora.domain.model.DateFormat
import com.finora.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import java.util.Locale
import javax.inject.Inject

/**
 * Implementation of UserPreferencesRepository using DataStore.
 */
class UserPreferencesRepositoryImpl @Inject constructor(
    private val context: Context
) : UserPreferencesRepository {
    
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = "user_preferences"
    )
    
    companion object {
        private val KEY_CURRENCY = stringPreferencesKey("currency_code")
        private val KEY_REGION = stringPreferencesKey("region_code")
        private val KEY_LANGUAGE = stringPreferencesKey("language_code")
    }
    
    override fun getUserPreferences(): Flow<UserPreferences> {
        return context.dataStore.data.map { preferences ->
            val currencyCode = preferences[KEY_CURRENCY]
            val regionCode = preferences[KEY_REGION]
            val languageCode = preferences[KEY_LANGUAGE] ?: "en"
            
            val currency = currencyCode?.let { Currency.fromCode(it) }
                ?: Currency.fromLocale(Locale.getDefault())
            
            val region = regionCode?.let { Region.fromCode(it) }
                ?: Region.fromLocale(Locale.getDefault())
            
            UserPreferences(
                currency = currency,
                region = region,
                languageCode = languageCode,
                dateFormat = DateFormat.fromRegion(region),
                lastUpdated = Date()
            )
        }
    }
    
    override suspend fun setCurrency(currency: Currency) {
        context.dataStore.edit { preferences ->
            preferences[KEY_CURRENCY] = currency.code
        }
    }
    
    override suspend fun setRegion(region: Region) {
        context.dataStore.edit { preferences ->
            preferences[KEY_REGION] = region.code
        }
    }
    
    override suspend fun setLanguage(languageCode: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_LANGUAGE] = languageCode
        }
    }
    
    override suspend fun resetToDefaults() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
