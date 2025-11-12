package com.finora.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.finora.data.local.converter.DateConverter
import com.finora.data.local.converter.StringListConverter
import com.finora.data.local.dao.ExpenseDao
import com.finora.data.local.entity.ExpenseEntity

@Database(
    entities = [ExpenseEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(DateConverter::class, StringListConverter::class)
abstract class FinoraDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
    
    companion object {
        const val DATABASE_NAME = "finora_database"
    }
}
