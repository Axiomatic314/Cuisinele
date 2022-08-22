package com.example.cuisinele

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cuisinele.databinding.SuccessPageBinding
import java.lang.String
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
}