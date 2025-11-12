package com.finora.core

import timber.log.Timber

object Logger {
    
    fun init(isDebug: Boolean) {
        if (isDebug) {
            Timber.plant(Timber.DebugTree())
        }
    }
    
    fun d(message: String, vararg args: Any?) {
        Timber.d(message, *args)
    }
    
    fun i(message: String, vararg args: Any?) {
        Timber.i(message, *args)
    }
    
    fun w(message: String, vararg args: Any?) {
        Timber.w(message, *args)
    }
    
    fun e(throwable: Throwable?, message: String, vararg args: Any?) {
        Timber.e(throwable, message, *args)
    }
    
    fun e(message: String, vararg args: Any?) {
        Timber.e(message, *args)
    }
    
    fun wtf(throwable: Throwable?, message: String, vararg args: Any?) {
        Timber.wtf(throwable, message, *args)
    }
}
