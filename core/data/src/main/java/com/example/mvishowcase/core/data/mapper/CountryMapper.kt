package com.example.mvishowcase.core.data.mapper

import com.example.mvishowcase.core.data.model.CountryDto
import com.example.mvishowcase.core.model.Country

fun CountryDto.toDomain(): Country {
    return Country(
        id = uuid,
        name = names.common,
        flag = flag.urlPng,
        capital = capitals.find { it.attributes.primary }?.name ?: capitals.firstOrNull()?.name ?: "Unknown",
        population = population,
        description = null
    )
}
