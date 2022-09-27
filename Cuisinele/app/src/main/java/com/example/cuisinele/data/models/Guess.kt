package com.example.cuisinele.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * Guess data class represents the Guess table in the database
 */
@Entity(tableName = "Guess")
data class Guess (
    @PrimaryKey(autoGenerate = true)
    val GuessID: Int,
    val DishID: Int,
    val CountryID: Int,
)