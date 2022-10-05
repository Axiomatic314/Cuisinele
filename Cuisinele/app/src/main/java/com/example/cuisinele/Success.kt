package com.example.cuisinele

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cuisinele.databinding.SuccessPageBinding
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
        Loading.getGuessData(binding.correctAnswer, textViews)
        Loading.setCountDown(binding.countdownTimer, binding.continueButton)
        setContinue()
        Loading.setScore(true, binding.scoreTextView)
        //todo add the required button, then uncomment this line
//        copyResults()
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

    //todo add the shareResults button, then uncomment this code
//    private fun copyResults(){
//        val results = Loading.getResults(true)
//        val clipboard = ContextCompat.getSystemService(requireContext(), ClipboardManager::class.java) as ClipboardManager
//        val clip = ClipData.newPlainText("results", results)
//        binding.shareResults.setOnClickListener {
//            clipboard.setPrimaryClip(clip)
//            //todo anchor this to an element of the success_page, so it can be seen
//            Snackbar.make(requireView(),"Copied to clipboard!",Snackbar.LENGTH_SHORT).show()
//        }
//    }

    /**
     * Method destroys the view, unsets the binding variable, and ensures the timer is cancelled.
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