package com.finora.domain.usecase

import android.net.Uri
import com.finora.core.AppError
import com.finora.core.DispatcherProvider
import com.finora.core.Result
import com.finora.domain.model.ReceiptData
import com.finora.domain.repository.OcrRepository
import javax.inject.Inject

class ScanReceiptUseCase @Inject constructor(
    private val ocrRepository: OcrRepository,
    dispatcherProvider: DispatcherProvider
) : UseCase<ScanReceiptUseCase.Params, ReceiptData>(dispatcherProvider) {
    
    data class Params(val imageUri: Uri)
    
    override suspend fun execute(params: Params): Result<ReceiptData, AppError> {
        return ocrRepository.processReceipt(params.imageUri)
    }
}
