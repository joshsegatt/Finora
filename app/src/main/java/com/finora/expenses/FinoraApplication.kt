package com.finora.expenses

import android.app.Application
import com.finora.core.Logger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FinoraApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        Logger.init(BuildConfig.DEBUG)
        Logger.d("Finora app started")
    }
}
