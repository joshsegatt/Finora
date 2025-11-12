package com.finora.expenses.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

data class OnboardingPage(val icon: androidx.compose.ui.graphics.vector.ImageVector, val title: String, val description: String)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(onComplete: () -> Unit) {
    val pages = listOf(
        OnboardingPage(Icons.Default.AttachMoney, "Track Your Expenses", "Scan receipts and track spending automatically with AI"),
        OnboardingPage(Icons.Default.Insights, "Smart Insights", "Get personalized recommendations to save money"),
        OnboardingPage(Icons.Default.TrendingUp, "Budget Goals", "Set budgets and reach your financial goals")
    )
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()
    
    Column(Modifier.fillMaxSize().padding(24.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            if (pagerState.currentPage < pages.size - 1) TextButton({ onComplete() }) { Text("Skip") }
        }
        HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { page ->
            Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Icon(pages[page].icon, null, Modifier.size(120.dp), tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.height(32.dp))
                Text(pages[page].title, style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
                Spacer(Modifier.height(16.dp))
                Text(pages[page].description, style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Row(Modifier.fillMaxWidth().padding(vertical = 24.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            repeat(pages.size) { i -> Box(Modifier.size(if (i == pagerState.currentPage) 12.dp else 8.dp).padding(4.dp), contentAlignment = Alignment.Center) { Surface(Modifier.fillMaxSize(), shape = MaterialTheme.shapes.small, color = if (i == pagerState.currentPage) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant) {} } }
            Button({ if (pagerState.currentPage < pages.size - 1) scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) } else onComplete() }) {
                Text(if (pagerState.currentPage < pages.size - 1) "Next" else "Get Started")
            }
        }
    }
}
