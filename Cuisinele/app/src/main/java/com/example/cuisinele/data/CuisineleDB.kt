package com.example.cuisinele.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cuisinele.data.models.Country
import com.example.cuisinele.data.models.Dish
import com.example.cuisinele.data.models.Hint
import kotlin.coroutines.CoroutineContext

@Database(entities = [Country::class, Dish::class, Hint::class], version = 1)
/** Class creates and populates the database. */
abstract class CuisineleDB : RoomDatabase() {
    /** Creates an instance of the DAO class */
    abstract fun cuisineleDAO(): CuisineleDAO

    companion object {
        var INSTANCE: CuisineleDB? = null

        /**
         * Build sqlite database based on prepopulated.db
         * @param context
         */
        fun getInstance(context: Context): CuisineleDB {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    CuisineleDB::class.java, "CuisineleDB.db"
                ).createFromAsset("prepopulated.db").allowMainThreadQueries().build()
            }
            return INSTANCE!!
        }
    }
}