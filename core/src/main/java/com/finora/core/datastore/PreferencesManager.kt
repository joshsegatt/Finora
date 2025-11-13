package com.finora.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "finora_preferences")

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
    private val DARK_MODE = booleanPreferencesKey("dark_mode")
    private val CURRENCY = stringPreferencesKey("currency")
    private val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
    
    val onboardingCompleted: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[ONBOARDING_COMPLETED] ?: false
        }
    
    suspend fun setOnboardingCompleted(completed: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[ONBOARDING_COMPLETED] = completed
        }
    }
    
    val darkMode: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[DARK_MODE] ?: false
        }
    
    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DARK_MODE] = enabled
        }
    }
    
    val currency: Flow<String> = context.dataStore.data
        .map { preferences ->
            val saved = preferences[CURRENCY]
            // Auto-detect on first use if not set or still default "USD"
            if (saved == null || (saved == "USD" && java.util.Locale.getDefault().country.uppercase() != "US")) {
                // Detect from device locale
                val countryCode = java.util.Locale.getDefault().country.uppercase()
                when (countryCode) {
                    "GB", "UK" -> "GBP"
                    "BR" -> "BRL"
                    "CH" -> "CHF"
                    "JP" -> "JPY"
                    "CA" -> "CAD"
                    "AU" -> "AUD"
                    "IN" -> "INR"
                    in listOf("AT", "BE", "CY", "EE", "FI", "FR", "DE", "GR", "IE", "IT",
                              "LV", "LT", "LU", "MT", "NL", "PT", "SK", "SI", "ES") -> "EUR"
                    else -> saved ?: "GBP" // Fallback to saved or GBP
                }
            } else {
                saved ?: "GBP"
            }
        }
    
    suspend fun setCurrency(currency: String) {
        context.dataStore.edit { preferences ->
            preferences[CURRENCY] = currency
        }
    }
    
    val notificationsEnabled: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[NOTIFICATIONS_ENABLED] ?: true
        }
    
    suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[NOTIFICATIONS_ENABLED] = enabled
        }
    }
}
