package com.example.cuisinele

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cuisinele.databinding.SuccessPageBinding

/**
 * Fragment class for the success page.
 *
 * This class acts as an end-screen if the user has successfully guessed the Cuisinele. It provides
 * the user's guesses and will show a countdown until the next Cuisinele.
 */
class Success : Fragment() {

    private var _binding: SuccessPageBinding? = null

    //This property is only valid between onCreateView and onDestroyView
    private val binding get() = _binding!!
    private lateinit var textViews: List<TextView>

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
        textViews = listOf(binding.guess1TextView, binding.guess2TextView, binding.guess3TextView, binding.guess4TextView, binding.guess5TextView, binding.guess6TextView)
        Loading.getGuessData(
            binding.correctAnswer,
            textViews
        )
        Loading.setCountDown(binding.countdownTimer, binding.continueButton)
        setContinue()
        return binding.root
    }

    /**
     * Hides the top action bar when the app is resumed.
     */
    override fun onResume() {
        super.onResume()
        MainActivity.hideTopBar()
    }

    /**
     * Navigates to the loading page if the app is paused.
     *
     * This ensures that the dish is updated if the date has changed.
     */
    override fun onPause() {
        super.onPause()
        MainActivity.showTopBar()
        findNavController().navigate(R.id.LoadingPage)
    }

    /**
     * Displays the continue button.
     *
     * If the game is not restricted to daily mode, it will immediately display the continue button.
     * Otherwise, it will wait for the countdown to end.
     */
    private fun setContinue() {
        if (!Settings.dailyGames) {
            binding.continueButton.visibility = View.VISIBLE
        }
        binding.continueButton.setOnClickListener {
            findNavController().navigate(R.id.LoadingPage)
        }
    }

    /**
     * Method destroys the view, unsets the binding variable, and ensures the timer is cancelled.
     */
    override fun onDestroyView() {
        Loading.timer.cancel()
        MainActivity.canGoBack = true
        super.onDestroyView()
        _binding = null
    }
}