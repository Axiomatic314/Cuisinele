package com.example.cuisinele

import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Settings class to hold static values.
 */
class Settings {
    companion object {
        /**
         * If dailyGames is set to false, there is no wait time between dishes.
         * If set to true, there will be one dish per day
         */
        val dailyGames: Boolean = true

        /**
         * This is the date the daily dishes begin from.
         *
         * This is used to ensure that all games are synced.
         */
        val startDate: LocalDate = LocalDate.parse("2022-09-13", DateTimeFormatter.ISO_LOCAL_DATE)
    }
}
