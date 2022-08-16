package com.example.cuisinele.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Country data class represents the Country table in the database
 */
@Entity(tableName = "Country")
data class Country(
    @PrimaryKey(autoGenerate = true)
    val CountryID: Int,
    val CountryName: String
)