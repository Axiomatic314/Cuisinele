package com.example.cuisinele

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cuisinele.databinding.SuccessPageBinding
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset
import kotlin.math.floor

/**
 * Fragment class for the success page.
 */
class Success : Fragment() {

    private var _binding: SuccessPageBinding? = null

    //This property is only valid between onCreateView and onDestroyView
    private val binding get() = _binding!!

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCountDown()
        setContinue()
    }

    //todo: stop the timer on page change, or check if on page before trying to write to textfield
    private fun setCountDown() {
        if (Settings.dailyGames) {
            val currentTime = LocalTime.now().toSecondOfDay()
            val secondsInDay = 86400
            val millisInFuture = ((secondsInDay - currentTime) *1000).toLong()
            val timer = object : CountDownTimer(millisInFuture, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    var message = "seconds remaining " + ((millisUntilFinished) / 1000).toString()
                    binding.countdownTimer.text = message
                }

                override fun onFinish() {
                    binding.continueButton.visibility = View.VISIBLE
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
        MainActivity.canGoBack = true
        super.onDestroyView()
        _binding = null
    }
}