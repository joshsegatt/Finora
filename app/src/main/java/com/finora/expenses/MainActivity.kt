package com.finora.expenses

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.finora.core.datastore.PreferencesManager
import com.finora.expenses.navigation.FinoraNavHost
import com.finora.expenses.onboarding.OnboardingScreen
import com.finora.ui.theme.FinoraTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    @Inject
    lateinit var preferencesManager: PreferencesManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        
        enableEdgeToEdge()
        
        setContent {
            val onboardingCompleted by preferencesManager.onboardingCompleted.collectAsState(initial = true)
            val darkMode by preferencesManager.darkMode.collectAsState(initial = false)
            val scope = rememberCoroutineScope()
            
            FinoraTheme(darkTheme = darkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (!onboardingCompleted) {
                        OnboardingScreen(
                            onComplete = {
                                scope.launch {
                                    preferencesManager.setOnboardingCompleted(true)
                                }
                            }
                        )
                    } else {
                        FinoraNavHost()
                    }
                }
            }
        }
    }
}
