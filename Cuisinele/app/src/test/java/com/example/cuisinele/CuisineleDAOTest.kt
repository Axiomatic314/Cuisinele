package com.example.cuisinele

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.cuisinele.data.CuisineleDAO
import com.example.cuisinele.data.CuisineleDB
import com.example.cuisinele.data.models.Country
import com.example.cuisinele.data.models.Dish
import com.example.cuisinele.data.models.Guess
import com.example.cuisinele.data.models.Hint
import junit.framework.TestCase
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Unit tests to test each method in CuisineleDAO
 */
@RunWith(AndroidJUnit4::class)
class CuisineleDAOTest: TestCase() {
    private lateinit var dao: CuisineleDAO
    private lateinit var database: CuisineleDB

    /**
     * Setup the database before any test is run
     */
    @Before
    fun setupDatabase() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            CuisineleDB::class.java
        ).allowMainThreadQueries().build()

        dao = database.cuisineleDAO()
    }

    /**
     * Kill the database session if it is active then the tests are complete
     */
    @After
    fun closeDatabase() {
        database.close()
    }

    /**
     * Run all the tests
     */
    @Test
    fun testDatabase() {
        testGetCountries()
        testGetDishById()
        testGetDishes()
        testGetHints()
        testUpdateDish()
    }

    /**
     * Test getting countries and setting all country related data
     */
    private fun testGetCountries() {
        var newCountryToAdd = Country(1, "New Zealand")
        dao.insertCountry(newCountryToAdd)

        newCountryToAdd = Country(2, "Australia")
        dao.insertCountry(newCountryToAdd)

        val countries = dao.getCountries()

        Assert.assertTrue("DAO getCountries method returned empty", countries.isNotEmpty())
        val country = countries.first()

        val dishToAdd = Dish(1, "Test dish", "image_url", country.CountryID, false, 0)
        dao.insertDish(dishToAdd)

        val guessToAdd = Guess(1, 1, country.CountryID)
        dao.insertGuess(guessToAdd)

        val hintToAdd = Hint(1, "Test hint", country.CountryID, false)
        dao.insertHint(hintToAdd)

        val countryByID = dao.getCountryByID(country.CountryID)
        Assert.assertTrue("Country with ID: ${country.CountryID} was not returned from DAO", countryByID != null)

        val countryByCountryName = dao.getCountryByName(country.CountryName)
        Assert.assertTrue("Country with name: ${country.CountryName} was not returned from DAO", countryByCountryName != null)
    }

    /**
     * Test getting dishes
     */
    private fun testGetDishes() {
        val dishes = dao.getDishes()
        Assert.assertTrue("DAO getDishes method returned empty", dishes.isNotEmpty())
    }

    /**
     * Test getting dish by its ID
     */
    private fun testGetDishById() {
        val dishes = dao.getDishes()
        Assert.assertTrue("DAO getDishes method returned an empty list", dishes.isNotEmpty())
        val dish = dao.getDishByID(dishes.first().DishID)
        Assert.assertTrue("Dish with ID: ${dishes.first().DishID} was not returned from DAO", dish != null)

        val guesses = dao.getGuessesByDishID(dishes.first().DishID)
        Assert.assertTrue("Guesses table returned empty", guesses.isNotEmpty())
    }

    /**
     * Test getting hints
     */
    private fun testGetHints() {
        val dishes = dao.getDishes()
        Assert.assertTrue("DAO getDishes method returned empty", dishes.isNotEmpty())
        val hints = dao.getHintsByDishID(dishes.first().DishID)
        Assert.assertTrue("No hints were returned for dish ID: ${dishes.first().DishID}", hints.isNotEmpty())
    }

    /**
     * Test updating a dish record
     */
    private fun testUpdateDish() {
        val dishes = dao.getDishes()
        Assert.assertTrue("DAO getDishes method returned nothing", dishes.isNotEmpty())
        var dish = dao.getDishByID(dishes.first().DishID)
        Assert.assertTrue("Dish with ID: ${dishes.first().DishID} was not returned from DAO", dish != null)

        val originalValue = dish!!.IsComplete
        dish.IsComplete = !dish.IsComplete
        dao.updateDish(dish)

        dish = dao.getDishByID(1)
        Assert.assertTrue("Dish with ID: 1 was not returned from DAO after updating its value", dish != null)


        Assert.assertTrue("Dish isComplete was not changed using updateDish DAO method", dish!!.IsComplete == !originalValue)

        dish.IsComplete = !dish.IsComplete
        dao.updateDish(dish)
    }
}