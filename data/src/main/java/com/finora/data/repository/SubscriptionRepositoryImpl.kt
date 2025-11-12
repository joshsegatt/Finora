package com.finora.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.finora.domain.model.Feature
import com.finora.domain.model.SubscriptionStatus
import com.finora.domain.model.SubscriptionTier
import com.finora.domain.repository.SubscriptionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

/**
 * Implementation of SubscriptionRepository using DataStore.
 * 
 * NOTE: This is a simplified version for testing.
 * Production version should integrate Google Play Billing Library.
 */
class SubscriptionRepositoryImpl @Inject constructor(
    private val context: Context
) : SubscriptionRepository {
    
    private val Context.subscriptionDataStore: DataStore<Preferences> by preferencesDataStore(
        name = "subscription_prefs"
    )
    
    companion object {
        private val KEY_TIER = stringPreferencesKey("subscription_tier")
        private val KEY_IS_ACTIVE = booleanPreferencesKey("is_active")
        private val KEY_EXPIRES_AT = longPreferencesKey("expires_at")
        private val KEY_PURCHASE_TOKEN = stringPreferencesKey("purchase_token")
        private val KEY_AUTO_RENEWING = booleanPreferencesKey("auto_renewing")
    }
    
    override fun getSubscriptionStatus(): Flow<SubscriptionStatus> {
        return context.subscriptionDataStore.data.map { preferences ->
            val tierName = preferences[KEY_TIER] ?: SubscriptionTier.FREE.name
            val tier = SubscriptionTier.entries.find { it.name == tierName }
                ?: SubscriptionTier.FREE
            
            val isActive = preferences[KEY_IS_ACTIVE] ?: false
            val expiresAtMs = preferences[KEY_EXPIRES_AT]
            val expiresAt = expiresAtMs?.let { Date(it) }
            val purchaseToken = preferences[KEY_PURCHASE_TOKEN]
            val autoRenewing = preferences[KEY_AUTO_RENEWING] ?: false
            
            SubscriptionStatus(
                tier = tier,
                isActive = isActive,
                expiresAt = expiresAt,
                purchaseToken = purchaseToken,
                autoRenewing = autoRenewing
            )
        }
    }
    
    override suspend fun isPremium(): Boolean {
        val status = getSubscriptionStatus().first()
        return status.isPremium()
    }
    
    override suspend fun hasAccess(feature: Feature): Boolean {
        val status = getSubscriptionStatus().first()
        return feature.isAccessible(status.tier)
    }
    
    override suspend fun updateSubscription(status: SubscriptionStatus) {
        context.subscriptionDataStore.edit { preferences ->
            preferences[KEY_TIER] = status.tier.name
            preferences[KEY_IS_ACTIVE] = status.isActive
            
            status.expiresAt?.let {
                preferences[KEY_EXPIRES_AT] = it.time
            }
            
            status.purchaseToken?.let {
                preferences[KEY_PURCHASE_TOKEN] = it
            }
            
            preferences[KEY_AUTO_RENEWING] = status.autoRenewing
        }
    }
    
    override suspend fun restorePurchases() {
        // TODO: Implement with Google Play Billing Library
        // For now, this is a no-op
        // In production:
        // 1. Query Play Billing for active purchases
        // 2. Verify with backend
        // 3. Call updateSubscription()
    }
    
    override suspend fun cancelSubscription() {
        context.subscriptionDataStore.edit { preferences ->
            preferences[KEY_IS_ACTIVE] = false
            preferences[KEY_AUTO_RENEWING] = false
        }
    }
}
