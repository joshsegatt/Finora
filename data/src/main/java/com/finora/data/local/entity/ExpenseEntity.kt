package com.finora.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.finora.data.local.converter.DateConverter
import com.finora.data.local.converter.StringListConverter
import java.util.Date

@Entity(tableName = "expenses")
@TypeConverters(DateConverter::class, StringListConverter::class)
data class ExpenseEntity(
    @PrimaryKey
    val id: String,
    val amount: Double,
    val category: String,
    val description: String,
    val date: Date,
    val merchant: String?,
    val receiptImagePath: String?,
    val tags: List<String>,
    val notes: String?,
    val createdAt: Date,
    val updatedAt: Date
)
