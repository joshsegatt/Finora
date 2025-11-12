package com.finora.domain.usecase

import com.finora.core.AppError
import com.finora.core.DispatcherProvider
import com.finora.core.Result
import kotlinx.coroutines.withContext
import javax.inject.Inject

abstract class UseCase<in P, out R>(
    private val dispatcherProvider: DispatcherProvider
) {
    suspend operator fun invoke(params: P): Result<R, AppError> {
        return try {
            withContext(dispatcherProvider.io) {
                execute(params)
            }
        } catch (e: Exception) {
            Result.Failure(AppError.fromThrowable(e))
        }
    }
    
    protected abstract suspend fun execute(params: P): Result<R, AppError>
}

abstract class FlowUseCase<in P, out R>(
    protected val dispatcherProvider: DispatcherProvider
) {
    abstract fun execute(params: P): R
}
