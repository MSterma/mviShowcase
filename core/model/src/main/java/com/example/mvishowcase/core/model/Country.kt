package com.example.mvishowcase.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Country(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("flag")
    val flag: String,
    @SerialName("capital")
    val capital: String,
    @SerialName("population")
    val population: Long,
    @SerialName("description")
    val description: String? = null
)
