package com.finora.domain.usecase

import com.finora.core.AppError
import com.finora.core.DispatcherProvider
import com.finora.core.Result
import com.finora.domain.model.Expense
import com.finora.domain.model.ExpenseCategory
import com.finora.domain.repository.ExpenseRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.Date

class SaveExpenseUseCaseTest {
    
    private lateinit var repository: ExpenseRepository
    private lateinit var useCase: SaveExpenseUseCase
    private val dispatcherProvider = DispatcherProvider(
        main = Dispatchers.Unconfined,
        io = Dispatchers.Unconfined,
        default = Dispatchers.Unconfined
    )
    
    @Before
    fun setup() {
        repository = mockk()
        useCase = SaveExpenseUseCase(repository, dispatcherProvider)
    }
    
    @Test
    fun `saves valid expense successfully`() = runTest {
        val expense = Expense(
            amount = 50.0,
            category = ExpenseCategory.FOOD,
            description = "Lunch",
            date = Date()
        )
        
        coEvery { repository.saveExpense(expense) } returns Result.Success(Unit)
        
        val result = useCase(SaveExpenseUseCase.Params(expense))
        
        assertTrue(result.isSuccess)
        coVerify { repository.saveExpense(expense) }
    }
    
    @Test
    fun `fails to save invalid expense`() = runTest {
        val invalidExpense = Expense(
            amount = -10.0,
            category = ExpenseCategory.FOOD,
            description = "",
            date = Date()
        )
        
        val result = useCase(SaveExpenseUseCase.Params(invalidExpense))
        
        assertTrue(result.isFailure)
        assertTrue(result.errorOrNull() is AppError.ValidationError)
    }
}
