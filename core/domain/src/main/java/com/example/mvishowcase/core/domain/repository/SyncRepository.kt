package com.example.mvishowcase.core.domain.repository

import com.example.mvishowcase.core.common.result.DataResult

interface SyncRepository {
    suspend fun syncCountries(query: String, limit: Int, offset: Int): DataResult<Unit>
}
