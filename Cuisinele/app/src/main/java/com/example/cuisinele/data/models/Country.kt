package com.example.cuisinele.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Country")
data class Country(
    @PrimaryKey(autoGenerate = true)
    val CountryID: Int,
    val CountryName: String
)