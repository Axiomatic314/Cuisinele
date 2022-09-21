package com.example.cuisinele

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.*
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cuisinele.databinding.CuisineleBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


/**
 * A fragment class which acts as the main game page and the default navigation page.
 */
class Cuisinele : Fragment() {

    private var _binding: CuisineleBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    //private var countries: Array<String> = arrayOf()
    private lateinit var countryAdapter: ArrayAdapter<String>
    private var guessNo = 1

    /**
     * Method creates and returns the view hierarchy associated with this fragment and sets up the AutoComplete.
     *
     * @return the full xml page to be displayed.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CuisineleBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        enterClicked()
//        toggleGuesses()
        populateHint()
    }

    /**
     * If application is resumed from background the data needs to be fetched in case the time has changed.
     */
    override fun onResume() {
        super.onResume()
        context?.let {
            countryAdapter =
                ArrayAdapter<String>(
                    it,
                    android.R.layout.simple_list_item_1,
                    LinkedList<String>()
                ).also { adapter ->
                    binding.countryTextField.setAdapter(adapter)
                    binding.countryTextField.threshold = 1
                }
        }
        getData()
    }

    /**
     * Function sets listener for the enter key. Updates the guesses in database and guess boxes with input
     * and navigates to success/failure page upon completion of the day's level
     */
    private fun enterClicked() {
        binding.countryTextField.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                if (Loading.countries.find { x -> x.CountryName == binding.countryTextField.text.toString() } != null) {
                    when (guessNo) {
                        1 -> {
                            binding.guess1TextView.text = binding.countryTextField.text
                            Loading.dish!!.GuessOne = Loading.getCountryID(binding.countryTextField.text.toString())
                        }
                        2 -> {
                            binding.guess2TextView.text = binding.countryTextField.text
                            Loading.dish!!.GuessTwo = Loading.getCountryID(binding.countryTextField.text.toString())
                        }
                        3 -> {
                            binding.guess3TextView.text = binding.countryTextField.text
                            Loading.dish!!.GuessThree = Loading.getCountryID(binding.countryTextField.text.toString())
                        }
                        4 -> {
                            binding.guess4TextView.text = binding.countryTextField.text
                            Loading.dish!!.GuessFour = Loading.getCountryID(binding.countryTextField.text.toString())
                        }
                        5 -> {
                            binding.guess5TextView.text = binding.countryTextField.text
                            Loading.dish!!.GuessFive = Loading.getCountryID(binding.countryTextField.text.toString())
                        }
                        6 -> {
                            Loading.dish!!.GuessSix = Loading.getCountryID(binding.countryTextField.text.toString())
                        }
                    }

                    if (binding.countryTextField.text.toString() == Loading.country!!.CountryName) {
                        Loading.dish!!.IsComplete = true
                        findNavController().navigate(R.id.SuccessPage)
                    } else if (guessNo == 6) {
                        Loading.dish!!.IsComplete = true
                        findNavController().navigate(R.id.FailurePage)
                    } else {
                        guessNo++
                    }
                    Loading.updateDish()
                    /** clears the input box after each incorrect guess and displays an 'incorrect' message to the user */
                    binding.countryTextField.text.clear()
                    Snackbar.make(
                        requireActivity().findViewById(R.id.countryTextField),
                        "Incorrect...",
                        Snackbar.LENGTH_SHORT
                    ).apply {
                        anchorView = requireActivity().findViewById(R.id.countryTextField)
                    }.show()
                }

                return@OnKeyListener true
            }
            false
        })
    }

    /**
     * This method uses data stored in Loading class to initialize game
     */
    private fun getData() {
        for (c in Loading.countries.sortedBy { x -> x.CountryName }) {
            if (countryAdapter.getPosition(c.CountryName) == -1)
                countryAdapter.add(c.CountryName)
        }
        countryAdapter.notifyDataSetChanged()

        if (Loading.dish != null) {
            if (!Loading.dish!!.IsComplete) {
                //fills the previous guess fields with old guesses store in the database
                populateImage()
                guessNo = 1
                binding.dishName.text = Loading.dish!!.DishName
                if (Loading.dish!!.GuessOne != 0) {
                    guessNo = 2
                    binding.guess1TextView.text = Loading.getCountryName(Loading.dish!!.GuessOne)
                }
                if (Loading.dish!!.GuessTwo != 0) {
                    guessNo = 3
                    binding.guess2TextView.text = Loading.getCountryName(Loading.dish!!.GuessTwo)
                }
                if (Loading.dish!!.GuessThree != 0) {
                    guessNo = 4
                    binding.guess3TextView.text = Loading.getCountryName(Loading.dish!!.GuessThree)
                }
                if (Loading.dish!!.GuessFour != 0) {
                    guessNo = 5
                    binding.guess4TextView.text = Loading.getCountryName(Loading.dish!!.GuessFour)
                }
                if (Loading.dish!!.GuessFive != 0) {
                    guessNo = 6
                    binding.guess5TextView.text = Loading.getCountryName(Loading.dish!!.GuessFive)
                }
            }
        }
    }

    /**
     * Function sets listeners for the three reveal hint buttons
     * and upon clicking hides them and reveals and displays the hint
     */
    private fun populateHint() {
        binding.hintButtonDisplay1.setOnClickListener {
            binding.hintButtonDisplay1.visibility = View.GONE
            binding.hintDisplay1.visibility = View.VISIBLE
            binding.hintDisplay1.text = Loading.hints!![0].HintText
        }
        binding.hintButtonDisplay2.setOnClickListener {
            binding.hintButtonDisplay2.visibility = View.GONE
            binding.hintDisplay2.visibility = View.VISIBLE
            binding.hintDisplay2.text = Loading.hints!![1].HintText
        }
        binding.hintButtonDisplay3.setOnClickListener {
            binding.hintButtonDisplay3.visibility = View.GONE
            binding.hintDisplay3.visibility = View.VISIBLE
            binding.hintDisplay3.text = Loading.hints!![2].HintText
        }
    }

    /**
     * Populates imageview
     */
    private fun populateImage() {
        GlobalScope.launch(Dispatchers.Main) {
            val decodedString: ByteArray =
                Base64.decode(Loading.dish!!.ImageUrl.split(",")[1], Base64.DEFAULT)
            val bitMap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            var imageView: ImageView = binding.cuisineleImage
            imageView.setImageBitmap(bitMap)
        }
    }

    /**
     * Method destroys the view and unsets the binding variable.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


//    /**
//     * Method sets up the show/hide guess button.
//     */
//    private fun toggleGuesses() {
//        binding.displayGuessButton.setOnClickListener {
//            if (binding.guessDisplay.visibility == View.INVISIBLE) {
//                binding.guessDisplay.visibility = View.VISIBLE
//                binding.displayGuessButton.text = getString(R.string.HideGuess)
//            } else {
//                binding.guessDisplay.visibility = View.INVISIBLE
//                binding.displayGuessButton.text = getString(R.string.DisplayGuess)
//            }
//        }
//    }

}