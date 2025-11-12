package com.finora.data.ocr

import org.junit.Assert.*
import org.junit.Test

class ReceiptParserTest {
    
    @Test
    fun `parses receipt with total amount`() {
        val text = """
            RESTAURANT ABC
            123 Main St
            Date: 11/12/2023
            
            Item 1       $15.50
            Item 2       $20.00
            
            SUBTOTAL     $35.50
            TAX          $3.55
            TOTAL        $39.05
            
            Thank you!
        """.trimIndent()
        
        val result = ReceiptParser.parse(text)
        
        assertNotNull(result.amount)
        assertEquals(39.05, result.amount!!, 0.01)
        assertNotNull(result.date)
        assertNotNull(result.merchant)
        assertTrue(result.confidence > 0.5f)
    }
    
    @Test
    fun `parses receipt with dollar sign`() {
        val text = """
            Store Name
            Total: $125.99
            Date: 01/15/2024
        """.trimIndent()
        
        val result = ReceiptParser.parse(text)
        
        assertNotNull(result.amount)
        assertEquals(125.99, result.amount!!, 0.01)
    }
    
    @Test
    fun `infers category from merchant name`() {
        val text = """
            PHARMACY PLUS
            Total: $25.50
        """.trimIndent()
        
        val result = ReceiptParser.parse(text)
        
        assertEquals(com.finora.domain.model.ExpenseCategory.HEALTHCARE, result.inferredCategory)
    }
    
    @Test
    fun `returns low confidence for incomplete data`() {
        val text = "Some random text without amount or date"
        
        val result = ReceiptParser.parse(text)
        
        assertTrue(result.confidence < 0.5f)
    }
    
    @Test
    fun `extracts merchant from first line`() {
        val text = """
            Target Store
            123 Main Street
            Total: $50.00
        """.trimIndent()
        
        val result = ReceiptParser.parse(text)
        
        assertTrue(result.merchant?.contains("Target") == true)
    }
    
    @Test
    fun `parses different date formats`() {
        val text1 = "Date: 12/31/2023 Total: $10.00"
        val text2 = "Date: 2023-12-31 Total: $10.00"
        
        val result1 = ReceiptParser.parse(text1)
        val result2 = ReceiptParser.parse(text2)
        
        assertNotNull(result1.date)
        assertNotNull(result2.date)
    }
}
