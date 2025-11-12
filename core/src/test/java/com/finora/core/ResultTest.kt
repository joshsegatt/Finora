package com.finora.core

import org.junit.Assert.*
import org.junit.Test

class ResultTest {
    
    @Test
    fun `success result returns data`() {
        val result = Result.Success(42)
        assertTrue(result.isSuccess)
        assertFalse(result.isFailure)
        assertEquals(42, result.getOrNull())
        assertNull(result.errorOrNull())
    }
    
    @Test
    fun `failure result returns error`() {
        val error = AppError.ValidationError("Invalid input")
        val result = Result.Failure(error)
        
        assertTrue(result.isFailure)
        assertFalse(result.isSuccess)
        assertNull(result.getOrNull())
        assertEquals(error, result.errorOrNull())
    }
    
    @Test
    fun `map transforms success value`() {
        val result = Result.Success(10)
        val mapped = result.map { it * 2 }
        
        assertEquals(20, mapped.getOrNull())
    }
    
    @Test
    fun `map preserves failure`() {
        val error = AppError.ParseError()
        val result: Result<Int, AppError> = Result.Failure(error)
        val mapped = result.map { it * 2 }
        
        assertEquals(error, mapped.errorOrNull())
    }
    
    @Test
    fun `flatMap chains operations`() {
        val result: Result<Int, AppError> = Result.Success(10)
        val chained = result.flatMap<Int> { value ->
            if (value > 5) Result.Success(value * 2)
            else Result.Failure(AppError.ValidationError("Too small"))
        }
        
        assertEquals(20, chained.getOrNull())
    }
    
    @Test
    fun `onSuccess executes on success`() {
        var executed = false
        Result.Success(42).onSuccess { executed = true }
        assertTrue(executed)
    }
    
    @Test
    fun `onFailure executes on failure`() {
        var executed = false
        Result.Failure(AppError.UnknownError()).onFailure { executed = true }
        assertTrue(executed)
    }
    
    @Test
    fun `of catches exceptions`() {
        val result = Result.of { throw IllegalStateException("Error") }
        assertTrue(result.isFailure)
        assertTrue(result.errorOrNull() is IllegalStateException)
    }
    
    @Test
    fun `asSuccess extension works`() {
        val result: Result<Int, AppError> = 42.asSuccess()
        assertEquals(42, result.getOrNull())
    }
    
    @Test
    fun `asFailure extension works`() {
        val result: Result<Int, AppError> = AppError.DatabaseError().asFailure()
        assertTrue(result.errorOrNull() is AppError.DatabaseError)
    }
}
