package com.example.cuisinele.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Hint data class represents the Hint table in the database
 */
@Entity(tableName = "Hint")
data class Hint(
    @PrimaryKey(autoGenerate = true)
    val HintID: Int,
    val HintText: String,
    val CountryID: Int
)
