package com.example.mvishowcase.core.domain.usecase

import com.example.mvishowcase.core.domain.repository.SyncRepository
import kotlinx.coroutines.flow.Flow

class ObserveSyncErrorsUseCase(private val repository: SyncRepository) {
    operator fun invoke(): Flow<String> = repository.syncErrors
}
