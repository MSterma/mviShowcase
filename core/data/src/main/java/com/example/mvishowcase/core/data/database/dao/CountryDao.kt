package com.example.mvishowcase.core.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.mvishowcase.core.data.database.entity.CountryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CountryDao {
    @Upsert
    suspend fun upsertCountries(countries: List<CountryEntity>)

    @Query("SELECT * FROM countries WHERE name LIKE '%' || :query || '%'")
    fun getCountries(query: String): Flow<List<CountryEntity>>

    @Query("SELECT * FROM countries WHERE id = :id")
    suspend fun getCountryById(id: String): CountryEntity?

    @Query("DELETE FROM countries")
    suspend fun clearAll()
}
