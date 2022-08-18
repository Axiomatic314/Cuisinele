package com.example.cuisinele.data

import androidx.room.Dao
import androidx.room.Insert
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
     * SQL Insert query to add
     * @param country into the database
     */
    @Insert
    fun insertCountry(country: Country)

    /**
     * SQL Insert query to add
     * @param dish into the database
     */
    @Insert
    fun insertDish(dish: Dish)

    /**
     * SQL Insert query to add
     * @param hint into the database
     */
    @Insert
    fun insertHint(hint: Hint)

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
     * SQL SELECT query to get a hint by id
     * @param hintID
     */
    @Query("SELECT h.* FROM Hint as h WHERE HintID = :hintID")
    fun getHintByID(hintID: Int): Hint?

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
     * SQL SELECT query to get all hints
     */
    @Query("SELECT * FROM Hint")
    fun getHints(): List<Hint>

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
     * SQL update query to update country record
     * @param country
     */
    @Update
    fun updateCountry(country: Country)

    /**
     * SQL update query to update dish record
     * @param dish
     */
    @Update
    fun updateDish(dish: Dish)

    /**
     * SQL update query to update hint record
     * @param hint
     */
    @Update
    fun updateHint(hint: Hint)

}