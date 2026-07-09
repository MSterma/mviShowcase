package com.example.mvishowcase.core.data.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.mvishowcase.core.domain.repository.WikipediaRepository

class SyncDescriptionWorker(
    appContext: Context,
    workerParams: WorkerParameters,
    private val wikipediaRepository: WikipediaRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val id = inputData.getString(KEY_ID) ?: return Result.failure()
        val name = inputData.getString(KEY_NAME) ?: return Result.failure()

        return try {
            wikipediaRepository.refreshCountryDescription(id, name)
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        const val KEY_ID = "country_id"
        const val KEY_NAME = "country_name"
    }
}
