package com.example.mvishowcase.core.data.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.mvishowcase.core.common.result.DataResult
import com.example.mvishowcase.core.domain.repository.SyncRepository

class SyncWorker(
    appContext: Context,
    workerParams: WorkerParameters,
    private val syncRepository: SyncRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val query = inputData.getString(KEY_QUERY) ?: ""
        val limit = inputData.getInt(KEY_LIMIT, 25)
        val offset = inputData.getInt(KEY_OFFSET, 0)

        return when (syncRepository.syncCountries(query, limit, offset)) {
            is DataResult.Success -> Result.success()
            is DataResult.Failure -> Result.retry()
        }
    }

    companion object {
        const val KEY_QUERY = "query"
        const val KEY_LIMIT = "limit"
        const val KEY_OFFSET = "offset"
    }
}
