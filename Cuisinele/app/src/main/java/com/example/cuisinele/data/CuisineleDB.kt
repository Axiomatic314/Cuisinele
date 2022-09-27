package com.example.cuisinele.data

import android.content.Context
import androidx.room.*
import androidx.room.migration.AutoMigrationSpec
import com.example.cuisinele.data.models.Country
import com.example.cuisinele.data.models.Dish
import com.example.cuisinele.data.models.Guess
import com.example.cuisinele.data.models.Hint

@Database(entities = [Country::class, Dish::class, Hint::class, Guess::class], version = 3, exportSchema = true,
    autoMigrations = [
        AutoMigration (from = 1, to = 2, spec = CuisineleDB.DishAutoMigration::class),
        AutoMigration(from = 2, to = 3)
    ])
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
                ).fallbackToDestructiveMigration()
                    .createFromAsset("prepopulated.db")
                    .allowMainThreadQueries()
                    .build()
            }
            return INSTANCE!!
        }
    }

    /**
     * Interface to assist with dish table migrations
     */
    @DeleteColumn(
        tableName = "Dish",
        columnName = "GuessOne"
    )
    @DeleteColumn(
        tableName = "Dish",
        columnName = "GuessTwo"
    )
    @DeleteColumn(
        tableName = "Dish",
        columnName = "GuessThree"
    )
    @RenameColumn(
        tableName = "Dish",
        fromColumnName = "GuessFour",
        toColumnName = "HintCount"
    )
    @DeleteColumn(
        tableName = "Dish",
        columnName = "GuessFive"
    )
    @DeleteColumn(
        tableName = "Dish",
        columnName = "GuessSix"
    )
    class DishAutoMigration: AutoMigrationSpec {   }
}
