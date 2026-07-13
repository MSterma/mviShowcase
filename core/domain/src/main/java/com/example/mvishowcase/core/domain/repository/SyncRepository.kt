package com.example.mvishowcase.core.domain.repository

import com.example.mvishowcase.core.common.result.DataResult

import kotlinx.coroutines.flow.Flow

interface SyncRepository {
    val syncErrors: Flow<String>
    val syncResults: Flow<Int>
    suspend fun syncCountries(query: String, limit: Int, offset: Int): DataResult<Unit>
}
