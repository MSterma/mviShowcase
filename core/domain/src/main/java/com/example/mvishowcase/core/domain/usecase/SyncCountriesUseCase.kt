package com.example.mvishowcase.core.domain.usecase

import android.content.Context

class SyncCountriesUseCase(
    private val context: Context,
    private val startSync: (Context, String, Int, Int) -> Unit
) {
    operator fun invoke(query: String = "", limit: Int = 25, offset: Int = 0) {
        startSync(context, query, limit, offset)
    }
}
