package com.example.cuisinele

import android.annotation.SuppressLint
import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cuisinele.data.ContextApplication
import com.example.cuisinele.data.CuisineleDAO
import com.example.cuisinele.data.CuisineleDB
import com.example.cuisinele.data.models.Country
import com.example.cuisinele.data.models.Dish
import com.example.cuisinele.data.models.Guess
import com.example.cuisinele.data.models.Hint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.util.concurrent.TimeUnit

/**
 * Fragment class for the loading page.
 *
 * This class is responsible for loading data from the database, and handling most
 * navigation inside the app.
 */
class Loading : Fragment(R.layout.loading_page) {

    /**
     * This is used to hold data from the database to be accessed globally.
     */
    companion object {
        /** Used to access the database. */
        lateinit var dao: CuisineleDAO
        /** The current dish for the day's Cuisinele. */
        var dish: Dish? = null
        /** The list of hints associated with the current Cuisinele. */
        var hints: List<Hint>? = null
        /** The country of the current dish.*/
        var country: Country? = null
        /** List of all valid countries to be used in the autocomplete.*/
        lateinit var countries: List<Country>
        /** Timer used to countdown to the next midnight.*/
        lateinit var timer: CountDownTimer
        /** The guesses for the current dish */
        var guesses: List<Guess> = listOf()

        /**
         * Updates the current dish used throughout the app.
         */
        fun updateDish() {
            GlobalScope.async {
                dao.updateDish(dish!!)
            }
        }

        /**
         * Updates a hint.
         */
        fun updateHint(hint: Hint) {
            GlobalScope.async {
                dao.updateHint(hint)
            }
        }

        /**
         * Gets the country ID from the corresponding name.
         *
         * @param[guessName] the name of the country the user has guessed.
         *
         * @return the ID of the country corresponding to the given country name.
         */
        fun getCountryID(guessName: String): Int {
            return dao.getCountryByName(guessName)!!.CountryID
        }

        /**
         * Gets the country name from the corresponding ID.
         *
         * @param[guessID] the ID of the country guessed by the user.
         *
         * @return the name of the country corresponding to the ID, or an empty string if there is none.
         */
        fun getCountryName(guessID: Int): String {
            return if (guessID != 0) {
                dao.getCountryByID(guessID)!!.CountryName
            } else {
                ""
            }
        }

        /**
         * This function displays the users guesses for the day in a table.
         */
        fun getGuessData(
            correctAnswer: TextView,
            textViews: List<TextView>
        ) {
            correctAnswer.text = country!!.CountryName
            for (i in guesses.indices) {
                textViews[i].text = getCountryName(guesses[i].CountryID)
            }
        }

        /**
         * This function sets up a countdown to the next midnight.
         *
         * Used to inform the user how long they have left until the next Cuisinele puzzle is available.
         * It gets the current time in seconds and the determines how many seconds are left until midnight.
         * Once the time passes midnight, a continue button is displayed so that the user may navigate back
         * to the main Cuisinele page.
         *
         * @param[countdownTimer] the TextView to hold the seconds left as a string.
         *
         * @param[continueButton] a button to navigate to the main Cuisinele page.
         */
        fun setCountDown(countdownTimer: TextView, continueButton: TextView) {
            if (Settings.dailyGames) {
                val currentTime = LocalTime.now().toSecondOfDay()
                val secondsInDay = 86400
                val millisInFuture = ((secondsInDay - currentTime) * 1000).toLong()
                timer = object : CountDownTimer(millisInFuture, 1000) {
                    @SuppressLint("DefaultLocale")
                    override fun onTick(millisUntilFinished: Long) {
                        var message = java.lang.String.format(
                            "Next Cuisinele in\n%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % TimeUnit.HOURS.toMinutes(1),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % TimeUnit.MINUTES.toSeconds(1)
                        )
                        countdownTimer.text = message
                    }

                    override fun onFinish() {
                        continueButton.visibility = View.VISIBLE
                        countdownTimer.visibility = View.INVISIBLE
                    }

                }.start()
            }
        }

        /**
         * Adds a new guess record for the current dish
         */
        fun addGuess(guess: Int) {
            GlobalScope.async {
                val newGuess = Guess(0, dish!!.DishID, guess)
                dao.insertGuess(newGuess)
                guesses = guesses.plus(newGuess)
            }
        }

        private fun calcScore(guesses: List<Guess>, hints: List<Hint>): Int {
            var score = 1050
            for (hint: Hint in hints) {
                if (hint.Activated) {
                    score -= 100
                }
            }
            for (guess: Guess in guesses) {
                score -= 50
            }
            return score
        }

        fun setScore(won: Boolean, textView: TextView) {
            var score = 0
            if (won){
                score = calcScore(guesses, hints!!)
            }
            dish!!.Score = score
            textView.text = "Your score is: $score"
        }
    }

    /**
     * Hides the top action bar and ensures the data in the companion object is up to date.
     */
    override fun onResume() {
        super.onResume()
        MainActivity.hideTopBar()
        getData()
    }

    /**
     * Reveals the top action bar.
     */
    override fun onPause() {
        super.onPause()
        MainActivity.showTopBar()
    }

    /**
     * This methods gets an instance of the CuisineleDAO and retrieves the appropriate information
     * from the database.
     *
     * If the game is set to daily mode (default), this method will get the dish for the
     * current day. Otherwise, it will fetch all dishes. The associated countries and hints will
     * also be retrieved.
     */
    private fun getData() {
        GlobalScope.launch(Dispatchers.IO) {
            dao = CuisineleDB.getInstance(ContextApplication.applicationContext()).cuisineleDAO()

            if (Settings.dailyGames) {
                // Convert current time since linux epoch from milliseconds to days
                val currentDate = LocalDate.now().toEpochDay()
                // The date we choose the dishes to start cycling from
                val cycleStartDate = Settings.startDate.toEpochDay()
                if (currentDate > cycleStartDate) {
                    // calculate the day since the dish cycle begun and use modulus of the number of dishes to allow recycling of dishes
                    val dishID: Int = ((currentDate - cycleStartDate) % dao.getDishes().size).toInt()
                    dish = dao.getDishByID(dishID)
                } else {
                    // TODO: add message/exception for when the dish cycle hasn't begun (this should never occur)
                }
            } else {
                val allDishes = dao.getDishes()
                dish = (allDishes.filter { x -> !x.IsComplete }).first()
            }

            GlobalScope.launch(Dispatchers.Main) {
                if (dish != null) {
                    country = dao.getCountryByID(dish!!.CountryID)
                    guesses = dao.getGuessesByDishID(dish!!.DishID)
                    if (dish!!.IsComplete) {
                        if (guesses.isNotEmpty()) {
                            if (guesses.size < 6) {
                                findNavController().navigate(R.id.SuccessPage)
                            } else {
                                if (guesses[5].CountryID == dish!!.CountryID) {
                                    findNavController().navigate(R.id.SuccessPage)
                                } else {
                                    findNavController().navigate(R.id.FailurePage)
                                }
                            }

                        } else {
                            findNavController().navigate(R.id.SuccessPage)
                        }
                    } else {
                        countries = dao.getCountries()
                        hints = dao.getHintsByDishID(dish!!.DishID)

                        findNavController().navigate(R.id.Home)
                    }
                }
            }
        }
    }

    /**
     * This method destroys the view.
     */
    override fun onDestroyView() {
        super.onDestroyView()
    }
}