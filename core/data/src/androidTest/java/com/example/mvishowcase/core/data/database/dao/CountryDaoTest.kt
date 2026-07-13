package com.example.mvishowcase.core.data.database.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.example.mvishowcase.core.data.database.CountryDatabase
import com.example.mvishowcase.core.data.database.entity.CountryEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CountryDaoTest {

    private lateinit var database: CountryDatabase
    private lateinit var dao: CountryDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            CountryDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.countryDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndGetCountries() = runTest {
        val countries = listOf(
            CountryEntity("1", "Poland", "flag", "Warsaw", 38000000),
            CountryEntity("2", "Germany", "flag", "Berlin", 83000000)
        )
        
        countries.forEach { dao.upsertCountry(it) }

        dao.getCountries("").test {
            val result = awaitItem()
            assertEquals(2, result.size)
            assertEquals("Poland", result.find { it.id == "1" }?.name)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun filterCountriesByName() = runTest {
        val countries = listOf(
            CountryEntity("1", "Poland", "flag", "Warsaw", 38000000),
            CountryEntity("2", "Germany", "flag", "Berlin", 83000000)
        )
        countries.forEach { dao.upsertCountry(it) }

        dao.getCountries("Pol").test {
            val result = awaitItem()
            assertEquals(1, result.size)
            assertEquals("Poland", result[0].name)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getCountryById() = runTest {
        val country = CountryEntity("1", "Poland", "flag", "Warsaw", 38000000)
        dao.upsertCountry(country)

        val result = dao.getCountryById("1")
        assertEquals("Poland", result?.name)
    }
}
