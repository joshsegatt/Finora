package com.finora.ui.theme

import androidx.compose.ui.graphics.Color
import com.finora.domain.model.ExpenseCategory

fun ExpenseCategory.getColor(): Color {
    return when (this) {
        ExpenseCategory.FOOD -> CategoryFood
        ExpenseCategory.TRANSPORTATION -> CategoryTransport
        ExpenseCategory.SHOPPING -> CategoryShopping
        ExpenseCategory.ENTERTAINMENT -> CategoryEntertainment
        ExpenseCategory.UTILITIES -> CategoryUtilities
        ExpenseCategory.HEALTHCARE -> CategoryHealthcare
        ExpenseCategory.GROCERIES -> CategoryGroceries
        ExpenseCategory.EDUCATION -> CategoryEducation
        ExpenseCategory.OTHER -> CategoryOther
    }
}
