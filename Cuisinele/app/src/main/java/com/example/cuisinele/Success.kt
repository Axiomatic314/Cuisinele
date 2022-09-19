package com.example.cuisinele

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cuisinele.data.ContextApplication
import com.example.cuisinele.data.CuisineleDAO
import com.example.cuisinele.data.CuisineleDB
import com.example.cuisinele.data.models.Country
import com.example.cuisinele.data.models.Dish
import com.example.cuisinele.data.models.Hint
import com.example.cuisinele.databinding.SuccessPageBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.String
import java.time.LocalDate
import java.time.LocalTime
import java.util.concurrent.TimeUnit

/**
 * Fragment class for the success page.
 */
class Success : Fragment() {

    private var _binding: SuccessPageBinding? = null

    //This property is only valid between onCreateView and onDestroyView
    private val binding get() = _binding!!
    private lateinit var timer : CountDownTimer
    private lateinit var dao: CuisineleDAO
    private var dish: Dish? = null
    private var hints: List<Hint>? = null
    private var country: Country? = null

    /**
     * Method creates and returns the view hierarchy associated with this fragment and inflates the page to be viewed.
     *
     * @return the full xml page to be displayed.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        MainActivity.canGoBack = false
        _binding = SuccessPageBinding.inflate(inflater, container, false)
        getData()
        setCountDown()
        setContinue()
        return binding.root
    }

    override fun onPause() {
        super.onPause()
        findNavController().navigate(R.id.LoadingPage)
    }

    private fun setCountDown() {
        if (Settings.dailyGames) {
            val currentTime = LocalTime.now().toSecondOfDay()
            val secondsInDay = 86400
            val millisInFuture = ((secondsInDay - currentTime) *1000).toLong()
            timer = object : CountDownTimer(millisInFuture, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    var message = String.format("Next Cuisinele in\n%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % TimeUnit.HOURS.toMinutes(1),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % TimeUnit.MINUTES.toSeconds(1)
                    )
                    binding.countdownTimer.text = message
                }

                override fun onFinish() {
                    binding.continueButton.visibility = View.VISIBLE
                    binding.countdownTimer.visibility = View.INVISIBLE
                }

            }.start()
        }
    }

    /**
     * Displays the continue button if the game is not restricted to daily mode.
     */
    private fun setContinue() {
        if (!Settings.dailyGames) {
            binding.continueButton.visibility = View.VISIBLE
        }
        binding.continueButton.setOnClickListener {
            findNavController().navigate(R.id.Home)
        }
    }

    /**
     * Method destroys the view and unsets the binding variable.
     */
    override fun onDestroyView() {
        timer.cancel()
        MainActivity.canGoBack = true
        super.onDestroyView()
        _binding = null
    }

    private fun getData() {
        GlobalScope.launch(Dispatchers.IO) {
            dao = CuisineleDB.getInstance(ContextApplication.applicationContext()).cuisineleDAO()
            if (Settings.dailyGames) {
                // Convert current time since linux epoch from milliseconds to days
                var currentDate = LocalDate.now().toEpochDay()
                // The date we choose the dishes to start cycling from
                var cycleStartDate = Settings.startDate.toEpochDay()
                if (currentDate > cycleStartDate) {
                    // calculate the day since the dish cycle begun and use modulus of the number of dishes to allow recycling of dishes
                    var dishID: Int = ((currentDate - cycleStartDate) % dao.getDishes().size).toInt()
                    dish = dao.getDishByID(dishID)
                } else {
                    // TODO: add message/exception for when the dish cycle hasn't begun (this should never occur)
                }
            } else {
                var allDishes = dao.getDishes()
                dish = (allDishes.filter { x -> !x.IsComplete }).first()
            }

            if (dish != null) {
                country = dao.getCountryByID(dish!!.CountryID)
                hints = dao.getHintsByDishID(dish!!.DishID)
                binding.correctAnswer.text = country!!.CountryName

                if (dish!!.GuessOne != 0) {
                    binding.guess1TextView.text = dao.getCountryByID(dish!!.GuessOne)!!.CountryName
                }
                if (dish!!.GuessTwo != 0) {
                    binding.guess2TextView.text = dao.getCountryByID(dish!!.GuessTwo)!!.CountryName
                }
                if (dish!!.GuessThree != 0) {
                    binding.guess3TextView.text = dao.getCountryByID(dish!!.GuessThree)!!.CountryName
                }
                if (dish!!.GuessFour != 0) {
                    binding.guess4TextView.text = dao.getCountryByID(dish!!.GuessFour)!!.CountryName
                }
                if (dish!!.GuessFive != 0) {
                    binding.guess5TextView.text = dao.getCountryByID(dish!!.GuessFive)!!.CountryName
                }
                if (dish!!.GuessSix != 0) {
                    binding.guess6TextView.text = dao.getCountryByID(dish!!.GuessSix)!!.CountryName
                }
            }
        }
    }

}