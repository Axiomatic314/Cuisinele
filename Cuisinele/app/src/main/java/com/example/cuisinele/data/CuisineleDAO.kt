package com.example.cuisinele.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.example.cuisinele.data.models.Country
import com.example.cuisinele.data.models.Dish
import com.example.cuisinele.data.models.Hint

/**
 * Data access object to interface with the database through sql queries
 */
@Dao
interface CuisineleDAO {

    /**
     * SQL SELECT query to get a country by id
     * @param countryID
     */
    @Query("SELECT c.* FROM Country as c WHERE CountryID = :countryID")
    fun getCountryByID(countryID: Int): Country?

    /**
     * SQL SELECT query to get a country by the country name
     * @param countryName
     */
    @Query("SELECT c.* FROM Country as c WHERE CountryName = :countryName")
    fun getCountryByName(countryName: String): Country?

    /**
     * SQL SELECT query to get a dish by id
     * @param dishID
     */
    @Query("SELECT d.* FROM Dish as d WHERE DishID = :dishID")
    fun getDishByID(dishID: Int): Dish?

    /**
     * SQL SELECT query to get all countries
     */
    @Query("SELECT * FROM Country")
    fun getCountries(): List<Country>

    /**
     * SQL SELECT query to get all dishes
     */
    @Query("SELECT * FROM Dish")
    fun getDishes(): List<Dish>

    /**
     * SQL SELECT query to get all hints by dishID
     * @param dishID
     */
    @Query(
        "SELECT h.* FROM Dish as d " +
                "INNER JOIN Country as c ON d.CountryID = c.CountryID " +
                "INNER JOIN Hint as h ON c.CountryID = h.CountryID " +
                "WHERE d.DishID = :dishID"
    )
    fun getHintsByDishID(dishID: Int): List<Hint>

    /**
     * SQL update query to update dish record
     * @param dish
     */
    @Update
    fun updateDish(dish: Dish)
}