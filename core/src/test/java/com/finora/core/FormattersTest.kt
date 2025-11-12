package com.finora.core

import org.junit.Assert.*
import org.junit.Test

class FormattersTest {
    
    @Test
    fun `CurrencyFormatter formats amount correctly`() {
        val formatted = CurrencyFormatter.format(123.45, "USD")
        assertTrue(formatted.contains("123.45") || formatted.contains("123,45"))
    }
    
    @Test
    fun `CurrencyFormatter parses valid amount`() {
        val amount = CurrencyFormatter.parse("$123.45")
        assertEquals(123.45, amount, 0.01)
    }
    
    @Test
    fun `CurrencyFormatter parses amount with comma`() {
        val amount = CurrencyFormatter.parse("123,45")
        assertEquals(123.45, amount, 0.01)
    }
    
    @Test
    fun `CurrencyFormatter parses plain number`() {
        val amount = CurrencyFormatter.parse("99.99")
        assertEquals(99.99, amount, 0.01)
    }
    
    @Test
    fun `CurrencyFormatter returns null for invalid input`() {
        val amount = CurrencyFormatter.parse("invalid")
        assertNull(amount)
    }
    
    @Test
    fun `DateFormatter formats date correctly`() {
        val date = java.util.Date(1700000000000L) // Fixed timestamp
        val formatted = DateFormatter.formatDate(date)
        assertTrue(formatted.matches(Regex("\\d{2}/\\d{2}/\\d{4}")))
    }
    
    @Test
    fun `DateFormatter parses valid date`() {
        val parsed = DateFormatter.parseDate("15/11/2023")
        assertNotNull(parsed)
    }
    
    @Test
    fun `DateFormatter returns null for invalid date`() {
        val parsed = DateFormatter.parseDate("invalid-date")
        assertNull(parsed)
    }
}
