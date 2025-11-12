package com.finora.features.reports

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finora.domain.model.Feature
import com.finora.domain.model.SubscriptionStatus
import com.finora.domain.model.SubscriptionTier
import com.finora.domain.repository.SubscriptionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class PaywallViewModel @Inject constructor(
    private val subscriptionRepository: SubscriptionRepository
) : ViewModel() {

    val subscriptionStatus: StateFlow<SubscriptionStatus?> = subscriptionRepository
        .getSubscriptionStatus()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun upgradeToPremium(tier: SubscriptionTier) {
        viewModelScope.launch {
            // TODO: Integrate with Google Play Billing
            // For now, just update local state for testing
            val newStatus = SubscriptionStatus(
                tier = tier,
                isActive = true,
                expiresAt = when (tier) {
                    SubscriptionTier.PREMIUM_MONTHLY -> Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000)
                    SubscriptionTier.PREMIUM_YEARLY -> Date(System.currentTimeMillis() + 365L * 24 * 60 * 60 * 1000)
                    else -> null
                },
                purchaseToken = "test_token_${System.currentTimeMillis()}",
                autoRenewing = true
            )
            subscriptionRepository.updateSubscription(newStatus)
        }
    }

    fun restorePurchases() {
        viewModelScope.launch {
            subscriptionRepository.restorePurchases()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaywallScreen(
    onDismiss: () -> Unit,
    viewModel: PaywallViewModel
) {
    val subscriptionStatus by viewModel.subscriptionStatus.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Upgrade to Premium") },
                actions = {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Text(
                text = "Unlock Premium Features",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Get unlimited access to all features and take control of your finances",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Premium Features
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Premium Features",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    PremiumFeatureItem(
                        icon = Icons.Default.AutoAwesome,
                        title = "AI Categorization",
                        description = "Smart auto-categorization with machine learning"
                    )

                    PremiumFeatureItem(
                        icon = Icons.Default.AccountBalance,
                        title = "Unlimited Budgets",
                        description = "Create as many budgets as you need (Free: 3)"
                    )

                    PremiumFeatureItem(
                        icon = Icons.Default.Insights,
                        title = "Advanced Insights",
                        description = "Deep analytics and spending predictions"
                    )

                    PremiumFeatureItem(
                        icon = Icons.Default.Notifications,
                        title = "Smart Notifications",
                        description = "Intelligent alerts based on your spending patterns"
                    )

                    PremiumFeatureItem(
                        icon = Icons.Default.Cloud,
                        title = "Cloud Backup",
                        description = "Secure cloud backup and sync across devices"
                    )

                    PremiumFeatureItem(
                        icon = Icons.Default.Download,
                        title = "Export Data",
                        description = "Export to CSV, Excel, PDF"
                    )

                    PremiumFeatureItem(
                        icon = Icons.Default.Category,
                        title = "Custom Categories",
                        description = "Create your own expense categories"
                    )

                    PremiumFeatureItem(
                        icon = Icons.Default.CurrencyExchange,
                        title = "Multi-Currency",
                        description = "Support for 9 global currencies"
                    )

                    PremiumFeatureItem(
                        icon = Icons.Default.Group,
                        title = "Shared Expenses",
                        description = "Split expenses with friends and family"
                    )
                }
            }

            // Pricing Cards
            Text(
                text = "Choose Your Plan",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )

            // Monthly Plan
            PricingCard(
                title = "Monthly",
                price = "£4.99",
                period = "/month",
                features = listOf(
                    "All Premium Features",
                    "Cancel Anytime",
                    "Priority Support"
                ),
                onSubscribe = { viewModel.upgradeToPremium(SubscriptionTier.PREMIUM_MONTHLY) },
                isPopular = false
            )

            // Yearly Plan (Popular)
            PricingCard(
                title = "Yearly",
                price = "£39.99",
                period = "/year",
                badge = "SAVE 33%",
                features = listOf(
                    "All Premium Features",
                    "2 Months FREE",
                    "Priority Support",
                    "Best Value"
                ),
                onSubscribe = { viewModel.upgradeToPremium(SubscriptionTier.PREMIUM_YEARLY) },
                isPopular = true
            )

            // Restore Purchases Button
            TextButton(
                onClick = { viewModel.restorePurchases() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Restore Purchases")
            }

            // Terms
            Text(
                text = "• Subscription automatically renews unless cancelled\n" +
                       "• Cancel anytime from Google Play Store\n" +
                       "• Payment charged to Google Play account",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun PremiumFeatureItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun PricingCard(
    title: String,
    price: String,
    period: String,
    features: List<String>,
    onSubscribe: () -> Unit,
    isPopular: Boolean,
    badge: String? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = if (isPopular) {
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        } else {
            CardDefaults.cardColors()
        }
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                
                if (badge != null) {
                    Surface(
                        color = MaterialTheme.colorScheme.secondary,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = badge,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = price,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = period,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            features.forEach { feature ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = feature,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Button(
                onClick = onSubscribe,
                modifier = Modifier.fillMaxWidth(),
                colors = if (isPopular) {
                    ButtonDefaults.buttonColors()
                } else {
                    ButtonDefaults.outlinedButtonColors()
                }
            ) {
                Text(if (isPopular) "Subscribe Now" else "Get Started")
            }
        }
    }
}
