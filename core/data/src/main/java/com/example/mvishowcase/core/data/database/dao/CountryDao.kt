package com.example.mvishowcase.core.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.mvishowcase.core.data.database.entity.CountryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CountryDao {

    @Upsert
    suspend fun upsertCountry(country: CountryEntity)

    @Transaction
    suspend fun upsertCountriesWithMerge(entities: List<CountryEntity>) {
        entities.forEach { entity ->
            val existing = getCountryById(entity.id)
            if (existing == null) {
                upsertCountry(entity)
            } else {
                val mergedEntity = entity.copy(
                    description = entity.description ?: existing.description
                )

                if (mergedEntity != existing) {
                    upsertCountry(mergedEntity)
                }
            }
        }
    }

    @Query("SELECT * FROM countries WHERE name LIKE '%' || :query || '%'")
    fun getCountries(query: String): Flow<List<CountryEntity>>

    @Query("SELECT * FROM countries WHERE id = :id")
    suspend fun getCountryById(id: String): CountryEntity?

    @Query("SELECT * FROM countries WHERE id = :id")
    fun getCountryByIdFlow(id: String): Flow<CountryEntity?>

    @Query("UPDATE countries SET description = :description WHERE id = :id")
    suspend fun updateCountryDescription(id: String, description: String?)

    @Query("DELETE FROM countries")
    suspend fun clearAll()
}
