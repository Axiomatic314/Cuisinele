package com.example.cuisinele

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cuisinele.databinding.FailurePageBinding

/**
 * Fragment class for the failure page.
 */
class Failure : Fragment() {

    private var _binding: FailurePageBinding? = null

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
        _binding = FailurePageBinding.inflate(inflater, container, false)
        MainActivity.hideTopBar()
        Loading.getGuessData(
            binding.correctAnswer,
            binding.guess1TextView,
            binding.guess2TextView,
            binding.guess3TextView,
            binding.guess4TextView,
            binding.guess5TextView,
            binding.guess6TextView
        )
        Loading.setCountDown(binding.countdownTimer, binding.continueButton)
        setContinue()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        MainActivity.hideTopBar()
    }

    /**
     * Navigates to the loading page if the app is paused.
     */
    override fun onPause() {
        super.onPause()
        MainActivity.showTopBar()
        findNavController().navigate(R.id.LoadingPage)
    }

    /**
     * Displays the continue button if the game is not restricted to daily mode.
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
     * Method destroys the view and unsets the binding variable.
     */
    override fun onDestroyView() {
        Loading.timer.cancel()
        MainActivity.canGoBack = true
        super.onDestroyView()
        _binding = null
    }
}