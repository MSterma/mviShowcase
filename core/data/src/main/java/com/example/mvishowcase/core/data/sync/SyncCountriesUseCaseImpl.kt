package com.example.mvishowcase.core.data.sync

import com.example.mvishowcase.core.domain.usecase.SyncCountriesUseCase
import com.example.mvishowcase.core.domain.usecase.SyncScheduler

class SyncCountriesUseCaseImpl(
    private val scheduler: SyncScheduler
) : SyncCountriesUseCase {
    override fun invoke(query: String, limit: Int, offset: Int) {
        try {
            scheduler.scheduleSync(query, limit, offset)
        } catch (e: Exception) {
            throw e
        }
    }
}
