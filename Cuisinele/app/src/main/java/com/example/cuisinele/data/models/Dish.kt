package com.example.cuisinele.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "Dish")
data class Dish(
    @PrimaryKey(autoGenerate = true)
    val DishID: Int,
    val DishName: String,
    val ImageUrl: String,
    val CountryID: Int,
    val IsComplete: Boolean,
    val GuessOne: Int,
    val GuessTwo: Int,
    val GuessThree: Int,
    val GuessFour: Int,
    val GuessFive: Int,
    val GuessSix: Int,
)
