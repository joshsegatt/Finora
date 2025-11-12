package com.finora.ml

import com.finora.core.AppError
import com.finora.core.DispatcherProvider
import com.finora.core.Result
import com.finora.domain.model.ExpenseCategory
import com.finora.domain.usecase.UseCase
import javax.inject.Inject

/**
 * SmartCategorizeUseCase - Sugestão inteligente de categoria.
 * 
 * IMPORTANTE: Esta é uma feature OPCIONAL e ADICIONAL.
 * - NÃO modifica comportamento padrão do app
 * - Usuário continua podendo escolher manualmente
 * - Apenas sugere categoria com base em IA
 * - Se falhar, retorna categoria baseada em regras
 */
class SmartCategorizeUseCase @Inject constructor(
    private val categoryPredictor: CategoryPredictor,
    dispatcherProvider: DispatcherProvider
) : UseCase<SmartCategorizeUseCase.Params, SmartCategorizeUseCase.CategorySuggestion>(dispatcherProvider) {
    
    data class Params(
        val description: String,
        val merchant: String? = null
    )
    
    data class CategorySuggestion(
        val category: ExpenseCategory,
        val confidence: Float,
        val isAIPrediction: Boolean
    )
    
    override suspend fun execute(params: Params): Result<CategorySuggestion, AppError> {
        return try {
            val prediction = categoryPredictor.predict(
                description = params.description,
                merchant = params.merchant
            )
            
            Result.Success(
                CategorySuggestion(
                    category = prediction.category,
                    confidence = prediction.confidence,
                    isAIPrediction = prediction.confidence > 0.6f
                )
            )
        } catch (e: Exception) {
            // Fallback: usa inferência baseada em regras
            val category = ExpenseCategory.inferFromText(params.description)
            Result.Success(
                CategorySuggestion(
                    category = category,
                    confidence = 0.5f,
                    isAIPrediction = false
                )
            )
        }
    }
}
