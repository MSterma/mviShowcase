package com.example.mvishowcase.core.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CountryResponse(
    val data: DataDto
)

@Serializable
data class DataDto(
    val objects: List<CountryDto>
)

@Serializable
data class CountryDto(
    val uuid: String,
    val names: NamesDto,
    val flag: FlagDto,
    val capitals: List<CapitalDto>,
    val population: Long
)

@Serializable
data class NamesDto(
    val common: String
)

@Serializable
data class FlagDto(
    @SerialName("url_png")
    val urlPng: String
)

@Serializable
data class CapitalDto(
    val name: String,
    val attributes: CapitalAttributesDto
)

@Serializable
data class CapitalAttributesDto(
    val primary: Boolean
)
