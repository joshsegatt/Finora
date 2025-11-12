package com.finora.data.repository

import android.content.Context
import android.net.Uri
import com.finora.core.AppError
import com.finora.core.Logger
import com.finora.core.Result
import com.finora.data.ocr.ReceiptParser
import com.finora.domain.model.ReceiptData
import com.finora.domain.repository.OcrRepository
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OcrRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : OcrRepository {
    
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    
    override suspend fun processReceipt(imageUri: Uri): Result<ReceiptData, AppError> {
        return try {
            Logger.d("Processing receipt from URI: $imageUri")
            
            val image = InputImage.fromFilePath(context, imageUri)
            val visionText = recognizer.process(image).await()
            
            val rawText = visionText.text
            Logger.d("OCR extracted text: ${rawText.take(200)}...")
            
            if (rawText.isBlank()) {
                Logger.w("No text detected in image")
                return Result.Failure(AppError.OcrError("No text detected in the image"))
            }
            
            val receiptData = ReceiptParser.parse(rawText)
            
            if (receiptData.confidence < 0.3f) {
                Logger.w("Low confidence receipt data: ${receiptData.confidence}")
            }
            
            Result.Success(receiptData)
        } catch (e: Exception) {
            Logger.e(e, "OCR processing failed")
            Result.Failure(AppError.OcrError("Failed to process receipt: ${e.message}"))
        }
    }
    
    override suspend fun processReceiptFromPath(imagePath: String): Result<ReceiptData, AppError> {
        return try {
            val file = File(imagePath)
            if (!file.exists()) {
                return Result.Failure(AppError.FileError("Image file not found"))
            }
            
            processReceipt(Uri.fromFile(file))
        } catch (e: Exception) {
            Logger.e(e, "Failed to process receipt from path")
            Result.Failure(AppError.FileError("Failed to process receipt: ${e.message}"))
        }
    }
}
