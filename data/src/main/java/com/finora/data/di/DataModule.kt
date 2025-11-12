package com.finora.data.di

import android.content.Context
import androidx.room.Room
import com.finora.data.local.dao.BudgetDao
import com.finora.data.local.dao.ExpenseDao
import com.finora.data.local.dao.NotificationDao
import com.finora.data.local.database.FinoraDatabase
import com.finora.data.local.database.MIGRATION_1_2
import com.finora.data.local.database.MIGRATION_2_3
import com.finora.data.repository.BudgetRepositoryImpl
import com.finora.data.repository.ExpenseRepositoryImpl
import com.finora.data.repository.NotificationRepositoryImpl
import com.finora.data.repository.OcrRepositoryImpl
import com.finora.domain.repository.BudgetRepository
import com.finora.domain.repository.ExpenseRepository
import com.finora.domain.repository.NotificationRepository
import com.finora.domain.repository.OcrRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    
    @Provides
    @Singleton
    fun provideFinoraDatabase(
        @ApplicationContext context: Context
    ): FinoraDatabase {
        return Room.databaseBuilder(
            context,
            FinoraDatabase::class.java,
            FinoraDatabase.DATABASE_NAME
        )
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
            .fallbackToDestructiveMigration()
            .build()
    }
    
    @Provides
    @Singleton
    fun provideExpenseDao(database: FinoraDatabase): ExpenseDao {
        return database.expenseDao()
    }
    
    @Provides
    @Singleton
    fun provideBudgetDao(database: FinoraDatabase): BudgetDao {
        return database.budgetDao()
    }
    
    @Provides
    @Singleton
    fun provideNotificationDao(database: FinoraDatabase): NotificationDao {
        return database.notificationDao()
    }
    
    @Provides
    @Singleton
    fun provideExpenseRepository(
        expenseDao: ExpenseDao
    ): ExpenseRepository {
        return ExpenseRepositoryImpl(expenseDao)
    }
    
    @Provides
    @Singleton
    fun provideBudgetRepository(
        budgetDao: BudgetDao
    ): BudgetRepository {
        return BudgetRepositoryImpl(budgetDao)
    }
    
    @Provides
    @Singleton
    fun provideNotificationRepository(
        notificationDao: NotificationDao
    ): NotificationRepository {
        return NotificationRepositoryImpl(notificationDao)
    }
    
    @Provides
    @Singleton
    fun provideOcrRepository(
        @ApplicationContext context: Context
    ): OcrRepository {
        return OcrRepositoryImpl(context)
    }
}