package com.example.mvishowcase.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mvishowcase.core.data.database.dao.CountryDao
import com.example.mvishowcase.core.data.database.entity.CountryEntity

@Database(entities = [CountryEntity::class], version = 2, exportSchema = false)
abstract class CountryDatabase : RoomDatabase() {
    abstract fun countryDao(): CountryDao
}
