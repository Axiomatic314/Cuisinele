package com.example.cuisinele.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cuisinele.data.models.Country
import com.example.cuisinele.data.models.Dish
import com.example.cuisinele.data.models.Hint

@Database(entities = [Country::class, Dish::class, Hint::class], version = 1)
abstract class CuisineleDB : RoomDatabase() {
    abstract fun cuisineleDAO(): CuisineleDAO

    companion object {
        var INSTANCE: CuisineleDB? = null

        fun getInstance(context: Context): CuisineleDB {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    CuisineleDB::class.java, "CuisineleDB.db"
                ).createFromAsset("prepopulated.db").build()
            }
            return INSTANCE!!
        }
    }
}