package com.example.cuisinele

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cuisinele.databinding.NoDishesPageBinding

class NoDishes: Fragment() {

    private var _binding: NoDishesPageBinding? = null

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
        _binding = NoDishesPageBinding.inflate(inflater, container, false)
        Loading.setCountDown(binding.countdownTimer, binding.continueButton)
        setContinue()
        return binding.root
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
     * This method destroys the view, cancels the timer, and unsets the binding variable.
     *
     */
    override fun onDestroyView() {
        if (Loading.timer != null) {
            Loading.timer!!.cancel()
        }
        MainActivity.canGoBack = true
        super.onDestroyView()
        _binding = null
    }
}