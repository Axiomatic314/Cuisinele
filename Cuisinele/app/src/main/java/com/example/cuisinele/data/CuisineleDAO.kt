package com.example.cuisinele.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.cuisinele.data.models.Country
import com.example.cuisinele.data.models.Dish
import com.example.cuisinele.data.models.Hint

@Dao
interface CuisineleDAO {
    @Insert
    fun insertCountry(country: Country)

    @Insert
    fun insertDish(dish: Dish)

    @Insert
    fun insertHint(hint: Hint)

    @Query("SELECT c.* FROM Country as c WHERE CountryID = :countryID")
    fun getCountryByID(countryID: Int): Country

    @Query("SELECT d.* FROM Dish as d WHERE DishID = :dishID")
    fun getDishByID(dishID: Int): Dish

    @Query("SELECT h.* FROM Hint as h WHERE HintID = :hintID")
    fun getHintByID(hintID: Int): Hint

    @Query("SELECT * FROM Country")
    fun getCountries(): List<Country>

    @Query("SELECT * FROM Dish")
    fun getDishs(): List<Dish>

    @Query("SELECT * FROM Hint")
    fun getHints(): List<Hint>

    @Query(
        "SELECT h.* FROM Dish as d " +
                "INNER JOIN Country as c ON d.CountryID = c.CountryID " +
                "INNER JOIN Hint as h ON c.CountryID = h.CountryID " +
                "WHERE d.DishID = :dishID"
    )
    fun getHintsByDishID(dishID: Int): List<Hint>

    @Update
    fun updateCountry(country: Country)

    @Update
    fun updateDish(dish: Dish)

    @Update
    fun updateHint(hint: Hint)

}