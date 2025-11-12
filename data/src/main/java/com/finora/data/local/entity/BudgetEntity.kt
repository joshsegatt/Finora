package com.finora.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**
 * BudgetEntity - Entidade Room para budgets.
 */
@Entity(tableName = "budgets")
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val category: String,
    val limitAmount: Double,
    val period: String, // MONTHLY ou ANNUAL
    val startDate: Date,
    val endDate: Date,
    val createdAt: Date,
    val isActive: Boolean
)
