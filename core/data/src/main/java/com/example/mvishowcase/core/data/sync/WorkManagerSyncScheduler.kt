package com.example.mvishowcase.core.data.sync

import android.content.Context
import androidx.work.*
import com.example.mvishowcase.core.domain.usecase.SyncScheduler
import java.util.concurrent.TimeUnit

class WorkManagerSyncScheduler(
    private val context: Context
) : SyncScheduler {

    override fun scheduleSync(query: String, limit: Int, offset: Int) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncData = workDataOf(
            SyncWorker.KEY_QUERY to query,
            SyncWorker.KEY_LIMIT to limit,
            SyncWorker.KEY_OFFSET to offset
        )

        val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                WorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .setInputData(syncData)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "sync_countries_${query}_${offset}",
            ExistingWorkPolicy.REPLACE,
            syncRequest
        )
    }
}
