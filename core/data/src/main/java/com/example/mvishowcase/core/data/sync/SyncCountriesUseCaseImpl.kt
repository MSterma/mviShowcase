package com.example.mvishowcase.core.data.sync

import com.example.mvishowcase.core.domain.usecase.SyncCountriesUseCase
import com.example.mvishowcase.core.domain.usecase.SyncScheduler

class SyncCountriesUseCaseImpl(
    private val scheduler: SyncScheduler
) : SyncCountriesUseCase {
    override fun invoke(query: String, limit: Int, offset: Int) {
        scheduler.scheduleSync(query, limit, offset)
    }
}
