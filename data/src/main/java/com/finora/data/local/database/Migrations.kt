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
