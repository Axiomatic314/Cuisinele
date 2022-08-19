package com.example.cuisinele

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cuisinele.data.ContextApplication
import com.example.cuisinele.data.CuisineleDAO
import com.example.cuisinele.data.CuisineleDB
import com.example.cuisinele.data.models.Country
import com.example.cuisinele.data.models.Dish
import com.example.cuisinele.data.models.Hint
import com.example.cuisinele.databinding.CuisineleBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.System.currentTimeMillis
import java.time.LocalDate
import java.util.*


/**
 * A fragment class which acts as the main game page and the default navigation page.
 */
class Cuisinele : Fragment() {

    private var _binding: CuisineleBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var dao: CuisineleDAO
    private var dish: Dish? = null
    private var hints: List<Hint>? = null
    private var country: Country? = null
    //private var countries: Array<String> = arrayOf()
    private lateinit var countryAdapter: ArrayAdapter<String>
    private var guessNo = 1

    /**
     * Method creates and returns the view hierarchy associated with this fragment and calls the keyboard setup function.
     *
     * @return the full xml page to be displayed.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CuisineleBinding.inflate(inflater, container, false)

        context?.let {
            countryAdapter =
                ArrayAdapter<String>(it, android.R.layout.simple_list_item_1, LinkedList<String>()).also { adapter ->
                    binding.countryTextField.setAdapter(adapter)
                    binding.countryTextField.threshold = 1
                }
        }
        //getData()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        enterClicked()
        toggleGuesses()
        populateHint()
    }

    /**
     * If application is resumed from background the data needs to be fetched in case the time has changed
     */
    override fun onResume() {
        super.onResume()
        getData()
    }

    private fun enterClicked() {
        binding.countryTextField.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                GlobalScope.async {
                    if (dao.getCountryByName(binding.countryTextField.text.toString()) != null) {
                        if (guessNo == 1) {
                            binding.guess1TextView.text = binding.countryTextField.text
                            dish!!.GuessOne = dao.getCountryByName(binding.countryTextField.text.toString())!!.CountryID
                            dao.updateDish(dish!!)
                        } else if (guessNo == 2) {
                            binding.guess2TextView.text = binding.countryTextField.text
                            dish!!.GuessTwo = dao.getCountryByName(binding.countryTextField.text.toString())!!.CountryID
                            dao.updateDish(dish!!)
                        } else if (guessNo == 3) {
                            binding.guess3TextView.text = binding.countryTextField.text
                            dish!!.GuessThree = dao.getCountryByName(binding.countryTextField.text.toString())!!.CountryID
                            dao.updateDish(dish!!)
                        } else if (guessNo == 4) {
                            binding.guess4TextView.text = binding.countryTextField.text
                            dish!!.GuessFour = dao.getCountryByName(binding.countryTextField.text.toString())!!.CountryID
                            dao.updateDish(dish!!)
                        } else if (guessNo == 5) {
                            binding.guess5TextView.text = binding.countryTextField.text
                            dish!!.GuessFive = dao.getCountryByName(binding.countryTextField.text.toString())!!.CountryID
                            dao.updateDish(dish!!)
                        } else if (guessNo == 6) {
                            dish!!.GuessSix = dao.getCountryByName(binding.countryTextField.text.toString())!!.CountryID
                            dao.updateDish(dish!!)
                        }

                        if (binding.countryTextField.text.toString() == country!!.CountryName) {
                            dish!!.IsComplete = true;
                            dao.updateDish(dish!!)
                            findNavController().navigate(R.id.SuccessPage)
                        } else if (guessNo == 6) {
                            dish!!.IsComplete = true;
                            dao.updateDish(dish!!)
                            findNavController().navigate(R.id.FailurePage)
                        } else {
                            guessNo++
                        }

                        binding.countryTextField.text.clear()
                        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        val view = activity?.currentFocus as View
                        imm.hideSoftInputFromWindow(view.windowToken, 0)
                    }
                }

                return@OnKeyListener true
            }
            false
        })
    }

    /**
     * This method gets an instance of CuisineleDAO and gets either
     * the daily dish or all dishes along with the associated country and hints
     */
    private fun getData() {
        GlobalScope.launch(Dispatchers.IO) {
            dao = CuisineleDB.getInstance(ContextApplication.applicationContext()).cuisineleDAO()

            for (c in dao.getCountries().sortedBy { x -> x.CountryName }) {
                if (countryAdapter.getPosition(c.CountryName) == -1)
                    countryAdapter.add(c.CountryName)
            }
            countryAdapter.notifyDataSetChanged()


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
                populateImage()
                guessNo = 1
                country = dao.getCountryByID(dish!!.CountryID)
                hints = dao.getHintsByDishID(dish!!.DishID)
                if (dish!!.GuessOne != 0) {
                    guessNo = 2
                    binding.guess1TextView.text = dao.getCountryByID(dish!!.GuessOne)!!.CountryName
                }
                if (dish!!.GuessTwo != 0) {
                    guessNo = 3
                    binding.guess2TextView.text = dao.getCountryByID(dish!!.GuessTwo)!!.CountryName
                }
                if (dish!!.GuessThree != 0) {
                    guessNo = 4
                    binding.guess3TextView.text = dao.getCountryByID(dish!!.GuessThree)!!.CountryName
                }
                if (dish!!.GuessFour != 0) {
                    guessNo = 5
                    binding.guess4TextView.text = dao.getCountryByID(dish!!.GuessFour)!!.CountryName
                }
                if (dish!!.GuessFive != 0) {
                    guessNo = 6
                    binding.guess5TextView.text = dao.getCountryByID(dish!!.GuessFive)!!.CountryName
                }

                if (dish!!.IsComplete) {
                    GlobalScope.launch(Dispatchers.Main) {
                        if (dish!!.GuessSix != 0) {
                            if (dish!!.GuessSix == dish!!.CountryID) {
                                findNavController().navigate(R.id.SuccessPage)
                            } else {
                                findNavController().navigate(R.id.FailurePage)
                            }
                        } else {
                            findNavController().navigate(R.id.SuccessPage)
                        }
                    }
                }

                // TODO: DELETE THIS LINE. It is for testing purposes
               // binding.hintDisplay.text = dish!!.DishName + " " + hints!![0].HintText + " " + country!!.CountryName

            }
        }
    }

    private fun populateHint(){
        var hintNumber = 0
        binding.displayHintButton.setOnClickListener {
            if (hintNumber == 0) {
                binding.hintDisplay1.text = hints!![hintNumber].HintText
                hintNumber++
            } else if (hintNumber == 1) {
                binding.hintDisplay2.text = hints!![hintNumber].HintText
                hintNumber++
            } else if (hintNumber == 2) {
                binding.hintDisplay3.text = hints!![hintNumber].HintText
                hintNumber++
            } else {
                Toast.makeText(context, "Out of hints!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Populates imageview
     */
    private fun populateImage() {
        GlobalScope.launch(Dispatchers.Main) {
            val decodedString: ByteArray = Base64.decode(dish!!.ImageUrl.split(",")[1], Base64.DEFAULT)
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

    /**
     * Method sets up the show/hide guess button.
     */
    private fun toggleGuesses() {
        binding.displayGuessButton.setOnClickListener {
            if (binding.guessDisplay.visibility == View.INVISIBLE) {
                binding.guessDisplay.visibility = View.VISIBLE
                binding.displayGuessButton.text = getString(R.string.HideGuess)
            } else {
                binding.guessDisplay.visibility = View.INVISIBLE
                binding.displayGuessButton.text = getString(R.string.DisplayGuess)
            }
        }
    }

}