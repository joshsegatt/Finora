package com.finora.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.finora.data.local.converter.DateConverter
import com.finora.data.local.converter.StringListConverter
import com.finora.data.local.dao.BudgetDao
import com.finora.data.local.dao.ExpenseDao
import com.finora.data.local.dao.NotificationDao
import com.finora.data.local.entity.BudgetEntity
import com.finora.data.local.entity.ExpenseEntity
import com.finora.data.local.entity.NotificationConfigEntity
import com.finora.data.local.entity.NotificationEntity
import com.finora.data.local.entity.NotificationScheduleEntity

@Database(
    entities = [
        ExpenseEntity::class, 
        BudgetEntity::class,
        NotificationEntity::class,
        NotificationConfigEntity::class,
        NotificationScheduleEntity::class
    ],
    version = 3,
    exportSchema = true
)
@TypeConverters(DateConverter::class, StringListConverter::class)
abstract class FinoraDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
    abstract fun budgetDao(): BudgetDao
    abstract fun notificationDao(): NotificationDao
    
    companion object {
        const val DATABASE_NAME = "finora_database"
    }
}
