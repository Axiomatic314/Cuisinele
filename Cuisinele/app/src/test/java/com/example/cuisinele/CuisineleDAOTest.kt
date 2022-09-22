package com.example.cuisinele

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.cuisinele.data.ContextApplication
import com.example.cuisinele.data.CuisineleDAO
import com.example.cuisinele.data.CuisineleDB
import com.example.cuisinele.data.models.Country
import com.example.cuisinele.data.models.Dish
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

    @Before
    fun setupDatabase() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            CuisineleDB::class.java
        ).allowMainThreadQueries().build()

        dao = database.cuisineleDAO()
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun testDatabase() {
        testGetCountries()
        testGetDishById()
        testGetDishes()
        testGetHints()
        testUpdateDish()
    }

    fun testGetCountries() {
        var newCountryToAdd = Country(1, "New Zealand")
        dao.insertCountry(newCountryToAdd)

        newCountryToAdd = Country(2, "Australia")
        dao.insertCountry(newCountryToAdd)

        var countries = dao.getCountries()

        Assert.assertTrue("DAO getCountries method returned empty", countries.isNotEmpty())
        var country = countries.first()

        var dishToAdd = Dish(1, "Test dish", "image_url", country.CountryID, false, 0, 0, 0, 0, 0, 0)
        dao.insertDish(dishToAdd)

        var hintToAdd = Hint(1, "Test hint", country.CountryID)
        dao.insertHint(hintToAdd)

        var countryByID = dao.getCountryByID(country.CountryID)
        Assert.assertTrue("Country with ID: ${country.CountryID} was not returned from DAO", countryByID != null)

        var countryByCountryName = dao.getCountryByName(country.CountryName)
        Assert.assertTrue("Country with name: ${country.CountryName} was not returned from DAO", countryByCountryName != null)
    }

    fun testGetDishes() {
        var dishes = dao.getDishes()
        Assert.assertTrue("DAO getDishes method returned empty", dishes.isNotEmpty())
    }

    fun testGetDishById() {
        var dishes = dao.getDishes()
        Assert.assertTrue("DAO getDishes method returned empty", dishes.isNotEmpty())
        var dish = dao.getDishByID(dishes.first().DishID)
        Assert.assertTrue("Dish with ID: 1 was not returned from DAO", dish != null)
    }

    fun testGetHints() {
        var dishes = dao.getDishes()
        Assert.assertTrue("DAO getDishes method returned empty", dishes.isNotEmpty())
        var hints = dao.getHintsByDishID(dishes.first().DishID)
        Assert.assertTrue("No hints were returned for dish ID: 1", hints.isNotEmpty())
    }

    fun testUpdateDish() {
        var dishes = dao.getDishes()
        Assert.assertTrue("DAO getDishes method returned empty", dishes.isNotEmpty())
        var dish = dao.getDishByID(dishes.first().DishID)
        Assert.assertTrue("Dish with ID: 1 was not returned from DAO", dish != null)

        var originalValue = dish!!.IsComplete
        dish!!.IsComplete = !dish!!.IsComplete
        dao.updateDish(dish)

        dish = dao.getDishByID(1)
        Assert.assertTrue("Dish with ID: 1 was not returned from DAO after updating its value", dish != null)


        Assert.assertTrue("Dish isComplete was not changed using updateDish DAO method", dish!!.IsComplete == !originalValue)

        dish!!.IsComplete = !dish!!.IsComplete
        dao.updateDish(dish)
    }
}