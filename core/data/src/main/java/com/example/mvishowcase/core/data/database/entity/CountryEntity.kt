package com.example.mvishowcase.core.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "countries")
data class CountryEntity(
    @PrimaryKey val id: String,
    val name: String,
    val flag: String,
    val capital: String,
    val population: Long,
    val description: String? = null
)
