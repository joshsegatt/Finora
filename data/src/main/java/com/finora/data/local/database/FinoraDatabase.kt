package com.finora.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
        
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Create budgets table with all columns and indices
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS budgets (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        category TEXT NOT NULL,
                        limitAmount REAL NOT NULL,
                        period TEXT NOT NULL,
                        startDate INTEGER NOT NULL,
                        endDate INTEGER NOT NULL,
                        createdAt INTEGER NOT NULL,
                        isActive INTEGER NOT NULL DEFAULT 1
                    )
                """.trimIndent())
                
                // Create indices for budgets table
                database.execSQL("""
                    CREATE INDEX IF NOT EXISTS index_budgets_category 
                    ON budgets(category)
                """.trimIndent())
                
                database.execSQL("""
                    CREATE INDEX IF NOT EXISTS index_budgets_isActive 
                    ON budgets(isActive)
                """.trimIndent())
            }
        }
        
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Create notifications table
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS notifications (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        type TEXT NOT NULL,
                        title TEXT NOT NULL,
                        message TEXT NOT NULL,
                        priority TEXT NOT NULL,
                        actionData TEXT,
                        isRead INTEGER NOT NULL DEFAULT 0,
                        createdAt INTEGER NOT NULL
                    )
                """.trimIndent())
                
                database.execSQL("""
                    CREATE INDEX IF NOT EXISTS index_notifications_type 
                    ON notifications(type)
                """.trimIndent())
                
                database.execSQL("""
                    CREATE INDEX IF NOT EXISTS index_notifications_isRead 
                    ON notifications(isRead)
                """.trimIndent())
                
                database.execSQL("""
                    CREATE INDEX IF NOT EXISTS index_notifications_createdAt 
                    ON notifications(createdAt)
                """.trimIndent())

                // Create notification_configs table
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS notification_configs (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        type TEXT NOT NULL,
                        isEnabled INTEGER NOT NULL DEFAULT 1,
                        updatedAt INTEGER NOT NULL
                    )
                """.trimIndent())
                
                database.execSQL("""
                    CREATE UNIQUE INDEX IF NOT EXISTS index_notification_configs_type 
                    ON notification_configs(type)
                """.trimIndent())

                // Create notification_schedules table
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS notification_schedules (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        type TEXT NOT NULL,
                        pattern TEXT NOT NULL,
                        nextTriggerAt INTEGER NOT NULL,
                        isActive INTEGER NOT NULL DEFAULT 1
                    )
                """.trimIndent())
                
                database.execSQL("""
                    CREATE INDEX IF NOT EXISTS index_notification_schedules_type 
                    ON notification_schedules(type)
                """.trimIndent())
                
                database.execSQL("""
                    CREATE INDEX IF NOT EXISTS index_notification_schedules_nextTriggerAt 
                    ON notification_schedules(nextTriggerAt)
                """.trimIndent())
            }
        }
    }
}
