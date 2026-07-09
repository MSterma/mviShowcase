package com.example.mvishowcase.core.domain.repository

interface WikipediaRepository {
    suspend fun refreshCountryDescription(id: String, name: String)
}
