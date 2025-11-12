package com.finora.domain.repository

import com.finora.domain.model.Feature
import com.finora.domain.model.SubscriptionStatus
import com.finora.domain.model.SubscriptionTier
import kotlinx.coroutines.flow.Flow

/**
 * Repository for managing subscription state.
 * 
 * TODO: Integrate with Google Play Billing in future.
 */
interface SubscriptionRepository {
    
    /**
     * Observe subscription status as Flow.
     */
    fun getSubscriptionStatus(): Flow<SubscriptionStatus>
    
    /**
     * Check if user has premium subscription.
     */
    suspend fun isPremium(): Boolean
    
    /**
     * Check if a specific feature is accessible.
     */
    suspend fun hasAccess(feature: Feature): Boolean
    
    /**
     * Update subscription (for testing/debug).
     * In production, this would be called after successful Google Play purchase.
     */
    suspend fun updateSubscription(status: SubscriptionStatus)
    
    /**
     * Restore purchases (Google Play).
     * TODO: Implement with Play Billing Library.
     */
    suspend fun restorePurchases()
    
    /**
     * Cancel subscription.
     */
    suspend fun cancelSubscription()
}
