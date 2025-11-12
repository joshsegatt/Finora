package com.finora.expenses.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.finora.core.ErrorHandler
import com.finora.core.SnackbarController
import com.finora.features.expenses.AddExpenseScreen
import com.finora.features.expenses.ExpenseListScreen
import com.finora.features.reports.ReportsScreen
import com.finora.expenses.settings.SettingsScreen

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    data object Expenses : Screen("expenses", "Expenses", Icons.Default.ReceiptLong)
    data object Reports : Screen("reports", "Reports", Icons.Default.Assessment)
    data object AddExpense : Screen("add_expense", "Add Expense", Icons.Default.ReceiptLong)
    data object Settings : Screen("settings", "Settings", Icons.Default.Settings)
}

@Composable
fun FinoraNavHost(
    navController: NavHostController = rememberNavController(),
    errorHandler: ErrorHandler = hiltViewModel<FinoraNavHostViewModel>().errorHandler,
    snackbarController: SnackbarController = hiltViewModel<FinoraNavHostViewModel>().snackbarController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    
    // Observar mensagens do SnackbarController
    LaunchedEffect(Unit) {
        snackbarController.messages.collect { message ->
            val result = snackbarHostState.showSnackbar(
                message = message.message,
                actionLabel = message.actionLabel,
                duration = if (message.actionLabel != null) SnackbarDuration.Long else SnackbarDuration.Short
            )
            if (result == SnackbarResult.ActionPerformed) {
                message.onAction?.invoke()
            }
        }
    }
    
    // Observar erros do ErrorHandler
    val errorEvent by errorHandler.errorEvents.collectAsStateWithLifecycle()
    LaunchedEffect(errorEvent) {
        errorEvent?.let { event ->
            val result = snackbarHostState.showSnackbar(
                message = event.message,
                actionLabel = if (event.canRetry) "Retry" else null,
                duration = if (event.canRetry) SnackbarDuration.Long else SnackbarDuration.Short
            )
            if (result == SnackbarResult.ActionPerformed) {
                event.retryAction?.invoke()
            }
            errorHandler.clearError()
        }
    }
    
    val bottomBarScreens = listOf(Screen.Expenses, Screen.Reports, Screen.Settings)
    val showBottomBar = currentDestination?.route in bottomBarScreens.map { it.route }
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomBarScreens.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                            label = { Text(screen.title) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Expenses.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Expenses.route) {
                ExpenseListScreen(
                    onNavigateToAddExpense = {
                        navController.navigate(Screen.AddExpense.route)
                    }
                )
            }
            
            composable(Screen.Reports.route) {
                ReportsScreen()
            }
            
            composable(Screen.AddExpense.route) {
                AddExpenseScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
            
            composable(Screen.Settings.route) {
                SettingsScreen()
            }
        }
    }
}
