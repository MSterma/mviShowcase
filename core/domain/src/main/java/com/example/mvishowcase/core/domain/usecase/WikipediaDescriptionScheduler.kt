package com.example.mvishowcase.core.domain.usecase

interface WikipediaDescriptionScheduler {
    fun scheduleRefresh(countryId: String, countryName: String)
}
