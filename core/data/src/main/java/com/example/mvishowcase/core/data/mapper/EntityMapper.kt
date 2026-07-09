package com.example.mvishowcase.core.data.mapper

import com.example.mvishowcase.core.data.database.entity.CountryEntity
import com.example.mvishowcase.core.model.Country

fun CountryEntity.toDomain(): Country {
    return Country(
        id = id,
        name = name,
        flag = flag,
        capital = capital,
        population = population,
        description = description
    )
}

fun Country.toEntity(): CountryEntity {
    return CountryEntity(
        id = id,
        name = name,
        flag = flag,
        capital = capital,
        population = population,
        description = description
    )
}
