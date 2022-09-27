package com.example.cuisinele

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.*
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cuisinele.databinding.CuisineleBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


/**
 * A fragment class which acts as the main game page.
 *
 * This class sets up the game of Cuisinele, and handles the user input.
 */
class Cuisinele : Fragment() {

    private var _binding: CuisineleBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var countryAdapter: ArrayAdapter<String>
    private var guessNo = 1
    private lateinit var textViews: List<TextView>


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
        textViews = listOf(binding.guess1TextView, binding.guess2TextView, binding.guess3TextView, binding.guess4TextView, binding.guess5TextView, binding.guess6TextView)
        return binding.root
    }

    /**
     * Ensures that the input submission and hints are set up.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        enterClicked()
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
     * This function sets a listener for the enter key.
     *
     * Updates the guesses in the database and fills the guess boxes with the user input. Also
     * navigates to success/failure page upon completion of the day's level.
     */
    private fun enterClicked() {
        binding.countryTextField.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                if (Loading.countries.find { x -> x.CountryName == binding.countryTextField.text.toString() } != null) {
                    if (guessNo <= 6) {
                        textViews[guessNo - 1].text = binding.countryTextField.text
                        Loading.addGuess(Loading.getCountryID(binding.countryTextField.text.toString()))
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
                    // clears the input box after each incorrect guess and displays an 'incorrect' message to the user
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
     * This method uses data stored in the Loading class to initialize the game.
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
                guessNo = if (Loading.guesses.size == 6) Loading.guesses.size else Loading.guesses.size + 1
                binding.dishName.text = Loading.dish!!.DishName

                for (i in Loading.guesses.indices) {
                    textViews[i].text = Loading.getCountryName(Loading.guesses[i].CountryID)
                }
            }
        }
    }

    /**
     * This function sets listeners for the three reveal hint buttons.
     *
     * When the buttons are clicked, they will be hidden and the hint displayed.
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
     * Populates the image of the current dish.
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
     * This method destroys the view and unsets the binding variable.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}