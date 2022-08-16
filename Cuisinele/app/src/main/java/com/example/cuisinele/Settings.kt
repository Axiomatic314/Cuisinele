package com.example.cuisinele

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Settings class to hold static values
 */
class Settings {
    companion object {
        /**
         * If dailyGames is set to false, there is no wait time between dishes.
         * If set to true, there will be one dish per day
         */
        val dailyGames: Boolean = true

        /**
         * Date we decide daily dishes begin
         */
        val startDate: LocalDate = LocalDate.parse("20220815", DateTimeFormatter.BASIC_ISO_DATE)
    }
}
