package com.example.cuisinele.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Dish data class represents the Dish table in the database
 */
@Entity(tableName = "Dish")
data class Dish(
    @PrimaryKey(autoGenerate = true)
    val DishID: Int,
    val DishName: String,
    val ImageUrl: String,
    val CountryID: Int,
    var IsComplete: Boolean,
    var GuessOne: Int,
    var GuessTwo: Int,
    var GuessThree: Int,
    var GuessFour: Int,
    var GuessFive: Int,
    var GuessSix: Int,
)
