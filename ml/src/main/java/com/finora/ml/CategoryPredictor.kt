package com.finora.ml

import android.content.Context
import com.finora.domain.model.ExpenseCategory
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * CategoryPredictor com FALLBACK obrigatório.
 * 
 * GARANTIAS DE SEGURANÇA:
 * 1. NUNCA lança exceção - sempre retorna categoria válida
 * 2. Se IA falhar, retorna ExpenseCategory.OTHER
 * 3. Se modelo não existir, usa inferência baseada em regras
 * 4. Confidence score indica confiabilidade (0.0 a 1.0)
 */
@Singleton
class CategoryPredictor @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    data class Prediction(
        val category: ExpenseCategory,
        val confidence: Float
    )
    
    // Flag para desativar IA se necessário
    private var mlEnabled = true
    
    /**
     * Prediz categoria com FALLBACK garantido.
     * NUNCA retorna null ou lança exceção.
     */
    suspend fun predict(description: String, merchant: String? = null): Prediction = withContext(Dispatchers.Default) {
        try {
            if (!mlEnabled) {
                return@withContext fallbackPrediction(description, merchant)
            }
            
            // Placeholder: quando modelo .tflite estiver disponível
            // val result = runTensorFlowModel(description, merchant)
            // if (result.confidence > 0.6f) return@withContext result
            
            // Por enquanto, usa fallback baseado em regras
            return@withContext fallbackPrediction(description, merchant)
            
        } catch (e: Exception) {
            // Qualquer erro: retorna fallback seguro
            return@withContext Prediction(
                category = ExpenseCategory.OTHER,
                confidence = 0.0f
            )
        }
    }
    
    /**
     * Fallback baseado em regras (sempre funciona).
     */
    private fun fallbackPrediction(description: String, merchant: String?): Prediction {
        val text = "${description.lowercase()} ${merchant?.lowercase() ?: ""}"
        
        // Usa lógica existente do ExpenseCategory
        val category = ExpenseCategory.inferFromText(text)
        
        // Confidence baseado em match de keywords
        val confidence = when (category) {
            ExpenseCategory.OTHER -> 0.3f  // Baixa confiança quando não reconhece
            else -> 0.7f  // Alta confiança quando match é encontrado
        }
        
        return Prediction(category, confidence)
    }
    
    /**
     * Desativa IA (usa apenas fallback).
     */
    fun disableML() {
        mlEnabled = false
    }
    
    /**
     * Ativa IA.
     */
    fun enableML() {
        mlEnabled = true
    }
}
