package com.finora.expenses.navigation

import androidx.lifecycle.ViewModel
import com.finora.core.ErrorHandler
import com.finora.core.SnackbarController
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FinoraNavHostViewModel @Inject constructor(
    val errorHandler: ErrorHandler,
    val snackbarController: SnackbarController
) : ViewModel()
