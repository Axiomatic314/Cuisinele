package com.example.cuisinele

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

class Loading : Fragment(R.layout.loading_page) {

    /**
     * Hold all database-related data globally
     */
    companion object {
        lateinit var dao: CuisineleDAO
        var dish: Dish? = null
        var hints: List<Hint>? = null
        var country: Country? = null
        lateinit var countries: List<Country>
        lateinit var timer: CountDownTimer

        /**
         * Updates a dish
         */
        fun updateDish() {
            GlobalScope.async {
                dao.updateDish(dish!!)
            }
        }

        /**
         * Returns country ID from matching country name
         */
        fun getCountryID(guessName: String): Int {
            return dao.getCountryByName(guessName)!!.CountryID
        }

        /**
         * Returns country name from matching country id
         */
        fun getCountryName(guessID: Int): String {
            return dao.getCountryByID(guessID)!!.CountryName
        }

        /**
         * Function displays the users guesses on for the day in a table.
         */
        fun getGuessData(correctAnswer: TextView, guess1TextView: TextView,
                            guess2TextView: TextView, guess3TextView: TextView,
                            guess4TextView: TextView, guess5TextView: TextView,
                            guess6TextView: TextView) {
            correctAnswer.text = country!!.CountryName
            if (dish!!.GuessOne != 0) {
                guess1TextView.text = getCountryName(dish!!.GuessOne)
            }
            if (dish!!.GuessTwo != 0) {
                guess2TextView.text = getCountryName(dish!!.GuessTwo)
            }
            if (dish!!.GuessThree != 0) {
                guess3TextView.text = getCountryName(dish!!.GuessThree)
            }
            if (dish!!.GuessFour != 0) {
                guess4TextView.text = getCountryName(dish!!.GuessFour)
            }
            if (dish!!.GuessFive != 0) {
                guess5TextView.text = getCountryName(dish!!.GuessFive)
            }
            if (dish!!.GuessSix != 0) {
                guess6TextView.text = getCountryName(dish!!.GuessSix)
            }
        }

        /**
         * Function gets the time from the current system time to the next day and displays it.
         * Once the time passes midnight, a continue button is displayed
         */
        fun setCountDown(countdownTimer: TextView, continueButton: TextView) {
            if (Settings.dailyGames) {
                val currentTime = LocalTime.now().toSecondOfDay()
                val secondsInDay = 86400
                val millisInFuture = ((secondsInDay - currentTime) * 1000).toLong()
                timer = object : CountDownTimer(millisInFuture, 1000) {
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

    override fun onResume() {
        super.onResume()
        getData()
    }

    /**
     * This method gets an instance of CuisineleDAO and gets either
     * the daily dish or all dishes along with the associated country and hints
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
                        country = dao.getCountryByID(dish!!.CountryID)
                        hints = dao.getHintsByDishID(dish!!.DishID)

                        findNavController().navigate(R.id.Home)
                    }
                }
            }
        }
    }

    /**
     * Method destroys the view and unsets the binding variable.
     */
    override fun onDestroyView() {
        super.onDestroyView()
    }
}