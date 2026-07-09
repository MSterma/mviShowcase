package com.example.mvishowcase.core.domain.usecase

import android.content.Context

interface SyncCountriesUseCase {
    operator fun invoke(query: String = "", limit: Int = 25, offset: Int = 0)
}
fun interface SyncScheduler {
    fun scheduleSync(query: String, limit: Int, offset: Int)
}
