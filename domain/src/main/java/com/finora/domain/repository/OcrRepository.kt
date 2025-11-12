package com.finora.domain.repository

import android.net.Uri
import com.finora.core.AppError
import com.finora.core.Result
import com.finora.domain.model.ReceiptData

interface OcrRepository {
    
    suspend fun processReceipt(imageUri: Uri): Result<ReceiptData, AppError>
    
    suspend fun processReceiptFromPath(imagePath: String): Result<ReceiptData, AppError>
}
