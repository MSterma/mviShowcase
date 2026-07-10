package com.example.mvishowcase.core.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CountryResponse(
    @SerialName("data")
    val data: DataDto
)

@Serializable
data class DataDto(
    @SerialName("objects")
    val objects: List<CountryDto>
)

@Serializable
data class CountryDto(
    @SerialName("uuid")
    val uuid: String,
    @SerialName("names")
    val names: NamesDto,
    @SerialName("flag")
    val flag: FlagDto,
    @SerialName("capitals")
    val capitals: List<CapitalDto>,
    @SerialName("population")
    val population: Long
)

@Serializable
data class NamesDto(
    @SerialName("common")
    val common: String
)

@Serializable
data class FlagDto(
    @SerialName("url_png")
    val urlPng: String
)

@Serializable
data class CapitalDto(
    @SerialName("name")
    val name: String,
    @SerialName("attributes")
    val attributes: CapitalAttributesDto
)

@Serializable
data class CapitalAttributesDto(
    @SerialName("primary")
    val primary: Boolean
)
