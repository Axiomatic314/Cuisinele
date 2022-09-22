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

        /**
         * Updates the current dish used throughout the app.
         */
        fun updateDish() {
            GlobalScope.async {
                dao.updateDish(dish!!)
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
            guess1TextView: TextView,
            guess2TextView: TextView,
            guess3TextView: TextView,
            guess4TextView: TextView,
            guess5TextView: TextView,
            guess6TextView: TextView
        ) {
            correctAnswer.text = country!!.CountryName
            guess1TextView.text = getCountryName(dish!!.GuessOne)
            guess2TextView.text = getCountryName(dish!!.GuessTwo)
            guess3TextView.text = getCountryName(dish!!.GuessThree)
            guess4TextView.text = getCountryName(dish!!.GuessFour)
            guess5TextView.text = getCountryName(dish!!.GuessFive)
            guess6TextView.text = getCountryName(dish!!.GuessSix)
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
                    val dishID: Int =
                        ((currentDate - cycleStartDate) % dao.getDishes().size).toInt()
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
                    if (dish!!.IsComplete) {
                        if (dish!!.GuessSix != 0) {
                            if (dish!!.GuessSix == dish!!.CountryID) {
                                findNavController().navigate(R.id.SuccessPage)
                            } else {
                                findNavController().navigate(R.id.FailurePage)
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