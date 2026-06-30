package com.example.mvishowcase.data.mapper

import com.example.mvishowcase.data.model.CountryDto
import com.example.mvishowcase.domain.model.Country

fun CountryDto.toDomain(): Country {
    return Country(
        id = uuid,
        name = names.common,
        flag = flag.urlPng,
        capital = capitals.find { it.attributes.primary }?.name ?: capitals.firstOrNull()?.name ?: "Unknown",
        population = population
    )
}
