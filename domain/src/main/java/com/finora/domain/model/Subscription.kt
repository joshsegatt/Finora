package com.finora.domain.model

import java.util.Date

/**
 * Available features in Finora.
 * Some features are FREE, others require PREMIUM subscription.
 */
enum class Feature(
    val displayName: String,
    val description: String,
    val requiresPremium: Boolean,
    val icon: String = "✨"
) {
    // FREE Features
    BASIC_EXPENSES(
        displayName = "Basic Expense Tracking",
        description = "Track unlimited expenses",
        requiresPremium = false,
        icon = "💰"
    ),
    OCR_SCANNING(
        displayName = "Receipt Scanning (OCR)",
        description = "Scan receipts with your camera",
        requiresPremium = false,
        icon = "📸"
    ),
    BASIC_BUDGETS(
        displayName = "Up to 3 Budgets",
        description = "Create basic monthly budgets",
        requiresPremium = false,
        icon = "🎯"
    ),
    BASIC_INSIGHTS(
        displayName = "Basic Insights",
        description = "Monthly spending reports",
        requiresPremium = false,
        icon = "📊"
    ),
    
    // PREMIUM Features
    AI_CATEGORIZATION(
        displayName = "AI Smart Categorization",
        description = "Let AI categorize expenses automatically",
        requiresPremium = true,
        icon = "🤖"
    ),
    UNLIMITED_BUDGETS(
        displayName = "Unlimited Budgets",
        description = "Create budgets for every category",
        requiresPremium = true,
        icon = "🎯"
    ),
    ADVANCED_INSIGHTS(
        displayName = "Advanced Insights & Predictions",
        description = "Monthly predictions, trends, anomaly detection",
        requiresPremium = true,
        icon = "🔮"
    ),
    SMART_NOTIFICATIONS(
        displayName = "Smart Notifications",
        description = "Budget alerts, spending spike warnings",
        requiresPremium = true,
        icon = "🔔"
    ),
    CLOUD_BACKUP(
        displayName = "Cloud Backup & Sync",
        description = "Automatic backup to cloud, multi-device sync",
        requiresPremium = true,
        icon = "☁️"
    ),
    EXPORT_DATA(
        displayName = "Export to CSV/Excel",
        description = "Export all your data for analysis",
        requiresPremium = true,
        icon = "📤"
    ),
    CUSTOM_CATEGORIES(
        displayName = "Custom Categories",
        description = "Create your own expense categories",
        requiresPremium = true,
        icon = "🏷️"
    ),
    MULTI_CURRENCY(
        displayName = "Multi-Currency Support",
        description = "Track expenses in multiple currencies",
        requiresPremium = true,
        icon = "💱"
    ),
    SHARED_EXPENSES(
        displayName = "Shared Expenses (Coming Soon)",
        description = "Split bills with friends/family",
        requiresPremium = true,
        icon = "🤝"
    );

    fun isAccessible(subscription: SubscriptionTier): Boolean {
        return !requiresPremium || subscription.isPremium
    }
}

/**
 * Subscription tiers.
 */
enum class SubscriptionTier(
    val displayName: String,
    val isPremium: Boolean,
    val monthlyPrice: Double = 0.0,  // in user's currency
    val yearlyPrice: Double = 0.0
) {
    FREE(
        displayName = "Free",
        isPremium = false
    ),
    PREMIUM_MONTHLY(
        displayName = "Premium Monthly",
        isPremium = true,
        monthlyPrice = 4.99,
        yearlyPrice = 0.0
    ),
    PREMIUM_YEARLY(
        displayName = "Premium Yearly",
        isPremium = true,
        monthlyPrice = 0.0,
        yearlyPrice = 39.99  // ~£3.33/month, 33% discount
    );
    
    fun getMonthlyEquivalent(): Double {
        return when (this) {
            FREE -> 0.0
            PREMIUM_MONTHLY -> monthlyPrice
            PREMIUM_YEARLY -> yearlyPrice / 12.0
        }
    }
}

/**
 * User's subscription status.
 */
data class SubscriptionStatus(
    val tier: SubscriptionTier = SubscriptionTier.FREE,
    val isActive: Boolean = false,
    val expiresAt: Date? = null,
    val purchaseToken: String? = null,  // For Google Play billing
    val autoRenewing: Boolean = false
) {
    fun isPremium(): Boolean = tier.isPremium && isActive
    
    fun isExpired(): Boolean {
        expiresAt ?: return !isActive
        return Date().after(expiresAt)
    }
    
    fun daysRemaining(): Int? {
        expiresAt ?: return null
        if (isExpired()) return 0
        
        val diff = expiresAt.time - Date().time
        return (diff / (1000 * 60 * 60 * 24)).toInt()
    }
}

/**
 * Free tier limits.
 */
object FreeTierLimits {
    const val MAX_BUDGETS = 3
    const val MAX_NOTIFICATIONS_PER_DAY = 5
    const val MAX_OCR_SCANS_PER_MONTH = 20
    
    // Premium has unlimited
    const val PREMIUM_UNLIMITED = Int.MAX_VALUE
}
