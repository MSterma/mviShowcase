package com.example.mvishowcase.core.data.mapper

import com.example.mvishowcase.core.data.database.entity.CountryEntity
import com.example.mvishowcase.core.model.Country
import org.junit.Assert.assertEquals
import org.junit.Test

class EntityMapperTest {

    @Test
    fun `entity toDomain maps correctly`() {
        val entity = CountryEntity(
            id = "1",
            name = "Poland",
            flag = "flag_url",
            capital = "Warsaw",
            population = 38000000
        )
        val domain = entity.toDomain()

        assertEquals(entity.id, domain.id)
        assertEquals(entity.name, domain.name)
        assertEquals(entity.flag, domain.flag)
        assertEquals(entity.capital, domain.capital)
        assertEquals(entity.population, domain.population)
    }

    @Test
    fun `domain toEntity maps correctly`() {
        val domain = Country(
            id = "1",
            name = "Poland",
            flag = "flag_url",
            capital = "Warsaw",
            population = 38000000
        )
        val entity = domain.toEntity()

        assertEquals(domain.id, entity.id)
        assertEquals(domain.name, entity.name)
        assertEquals(domain.flag, entity.flag)
        assertEquals(domain.capital, entity.capital)
        assertEquals(domain.population, entity.population)
    }
}
