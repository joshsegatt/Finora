package com.finora.data.local.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Migration 1 -> 2: Adiciona tabela budgets.
 * 
 * SEGURANÇA:
 * - NÃO toca tabela expenses existente
 * - Apenas ADICIONA nova tabela
 * - Rollback: apenas dropar tabela budgets
 */
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Criar tabela budgets
        db.execSQL("""
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
        
        // Índice para busca rápida por categoria
        db.execSQL("""
            CREATE INDEX IF NOT EXISTS index_budgets_category 
            ON budgets(category)
        """.trimIndent())
        
        // Índice para busca de budgets ativos
        db.execSQL("""
            CREATE INDEX IF NOT EXISTS index_budgets_isActive 
            ON budgets(isActive)
        """.trimIndent())
    }
}

/**
 * Migration 2 -> 3: Adiciona tabelas de notificações.
 * 
 * SEGURANÇA:
 * - NÃO toca tabelas existentes (expenses, budgets)
 * - Apenas ADICIONA 3 novas tabelas
 * - Rollback: apenas dropar tabelas notification_*
 */
val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Criar tabela notifications
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS notifications (
                id TEXT PRIMARY KEY NOT NULL,
                title TEXT NOT NULL,
                message TEXT NOT NULL,
                type TEXT NOT NULL,
                priority TEXT NOT NULL,
                triggerDate INTEGER NOT NULL,
                isRead INTEGER NOT NULL DEFAULT 0,
                actionDataJson TEXT,
                createdAt INTEGER NOT NULL
            )
        """.trimIndent())
        
        // Índice para busca rápida de não lidas
        db.execSQL("""
            CREATE INDEX IF NOT EXISTS index_notifications_isRead 
            ON notifications(isRead)
        """.trimIndent())
        
        // Índice para ordenação por data
        db.execSQL("""
            CREATE INDEX IF NOT EXISTS index_notifications_triggerDate 
            ON notifications(triggerDate DESC)
        """.trimIndent())
        
        // Criar tabela notification_config
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS notification_config (
                id TEXT PRIMARY KEY NOT NULL,
                budgetAlertsEnabled INTEGER NOT NULL DEFAULT 1,
                budgetAlertThreshold REAL NOT NULL DEFAULT 0.8,
                dailySummaryEnabled INTEGER NOT NULL DEFAULT 0,
                dailySummaryTime TEXT NOT NULL DEFAULT '20:00',
                weeklySummaryEnabled INTEGER NOT NULL DEFAULT 1,
                weeklySummaryDay INTEGER NOT NULL DEFAULT 1,
                weeklySummaryTime TEXT NOT NULL DEFAULT '09:00',
                monthlySummaryEnabled INTEGER NOT NULL DEFAULT 1,
                monthlySummaryDay INTEGER NOT NULL DEFAULT 1,
                monthlySummaryTime TEXT NOT NULL DEFAULT '10:00',
                insightAlertsEnabled INTEGER NOT NULL DEFAULT 1,
                spendingSpikeSensitivity REAL NOT NULL DEFAULT 1.5,
                reminderEnabled INTEGER NOT NULL DEFAULT 0,
                reminderFrequencyDays INTEGER NOT NULL DEFAULT 3,
                updatedAt INTEGER NOT NULL
            )
        """.trimIndent())
        
        // Inserir configuração padrão
        db.execSQL("""
            INSERT OR REPLACE INTO notification_config (
                id, budgetAlertsEnabled, budgetAlertThreshold, dailySummaryEnabled,
                dailySummaryTime, weeklySummaryEnabled, weeklySummaryDay, weeklySummaryTime,
                monthlySummaryEnabled, monthlySummaryDay, monthlySummaryTime,
                insightAlertsEnabled, spendingSpikeSensitivity, reminderEnabled,
                reminderFrequencyDays, updatedAt
            ) VALUES (
                'default', 1, 0.8, 0, '20:00', 1, 1, '09:00',
                1, 1, '10:00', 1, 1.5, 0, 3, ${System.currentTimeMillis()}
            )
        """.trimIndent())
        
        // Criar tabela notification_schedules
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS notification_schedules (
                id TEXT PRIMARY KEY NOT NULL,
                type TEXT NOT NULL,
                scheduledDate INTEGER NOT NULL,
                isRecurring INTEGER NOT NULL DEFAULT 0,
                recurringPattern TEXT,
                createdAt INTEGER NOT NULL
            )
        """.trimIndent())
        
        // Índice para ordenação por data agendada
        db.execSQL("""
            CREATE INDEX IF NOT EXISTS index_notification_schedules_scheduledDate 
            ON notification_schedules(scheduledDate ASC)
        """.trimIndent())
    }
}
