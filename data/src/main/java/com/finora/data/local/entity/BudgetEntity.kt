package com.finora.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

/**
 * BudgetEntity - Entidade Room para budgets.
 */
@Entity(
    tableName = "budgets",
    indices = [
        Index(value = ["category"]),
        Index(value = ["isActive"])
    ]
)
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val category: String,
    val limitAmount: Double,
    val period: String, // MONTHLY ou ANNUAL
    val startDate: Date,
    val endDate: Date,
    val createdAt: Date,
    @ColumnInfo(defaultValue = "1")
    val isActive: Boolean = true
)
