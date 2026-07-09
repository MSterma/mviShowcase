package com.example.mvishowcase.core.data.sync

import android.content.Context
import androidx.work.*
import com.example.mvishowcase.core.domain.usecase.WikipediaDescriptionScheduler
import java.util.concurrent.TimeUnit

class WorkManagerWikipediaDescriptionScheduler(
    private val context: Context
) : WikipediaDescriptionScheduler {

    override fun scheduleRefresh(countryId: String, countryName: String) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val data = workDataOf(
            SyncDescriptionWorker.KEY_ID to countryId,
            SyncDescriptionWorker.KEY_NAME to countryName
        )

        val request = OneTimeWorkRequestBuilder<SyncDescriptionWorker>()
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                WorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .setInputData(data)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "refresh_description_$countryId",
            ExistingWorkPolicy.REPLACE,
            request
        )
    }
}
