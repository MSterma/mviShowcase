package com.example.mvishowcase.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Country(
    val id: String,
    val name: String,
    val flag: String,
    val capital: String,
    val population: Long,
    val description: String? = null
)
