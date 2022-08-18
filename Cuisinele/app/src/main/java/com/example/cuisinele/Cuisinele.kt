package com.example.cuisinele

import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
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
import java.util.LinkedList

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
    private var countries: Array<String> = arrayOf()
    private lateinit var countryAdapter: ArrayAdapter<String>

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
        //setKeyButtons()
        setKeyButtons()

        context?.let {
            countryAdapter = ArrayAdapter<String>(it, android.R.layout.simple_list_item_1, LinkedList<String>()).also { adapter ->
                binding.countryTextField.setAdapter(adapter)
                binding.countryTextField.threshold = 1
            }
        }

        getData()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    /**
     * This method gets an instance of CuisineleDAO and gets either
     * the daily dish or all dishes along with the associated country and hints
     */
    private fun getData() {
        GlobalScope.launch(Dispatchers.IO) {
            dao = CuisineleDB.getInstance(ContextApplication.applicationContext()).cuisineleDAO()

            for (c in dao.getCountries().sortedBy { x -> x.CountryName }) {
                countryAdapter.add(c.CountryName)
            }
            countryAdapter.notifyDataSetChanged()


            if (Settings.dailyGames) {
                // Convert current time since linux epoch from milliseconds to days
                var currentDate = ((((currentTimeMillis() / 1000) / 60) / 60) / 24)
                // The date we choose the dishes to start cycling from
                var cycleStartDate = Settings.startDate.toEpochDay()
                if (currentDate > cycleStartDate) {
                    // calculate the day since the dish cycle begun and use modulus of the number of dishes to allow recycling of dishes
                    var dishID: Int = ((currentDate - cycleStartDate) % dao.getDishes().size).toInt()
                    dish = dao.getDishByID(dishID)
                } else {
                    // TODO: add message/exception for when the dish cycle hasnt begun (this should never occur)
                }
            } else {
                var allDishes = dao.getDishes()
                dish = (allDishes.filter { x -> !x.IsComplete }).first()
            }

            if (dish != null) {
                country = dao.getCountryByID(dish!!.CountryID)
                hints = dao.getHintsByDishID(dish!!.DishID)

                // TODO: DELETE THIS LINE. It is for testing purposes
                binding.textView.text = dish!!.DishName + " " + hints!![0].HintText + " " + country!!.CountryName
            }
        }
    }

    /**
     * Method destroys the view and unsets the binding variable.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

     //todo: remove
     //Sets up the keyboard listeners.
     //Creates each individual button's listener with a call to the write method and sets up functionality for the clear and delete buttons.
    /*fun setKeyButtons() {
        binding.keyboardA.setOnClickListener {
            writeChar("A")
        }
        binding.keyboardB.setOnClickListener {
            writeChar("B")
        }
        binding.keyboardC.setOnClickListener {
            writeChar("C")
        }
        binding.keyboardD.setOnClickListener {
            writeChar("D")
        }
        binding.keyboardE.setOnClickListener {
            writeChar("E")
        }
        binding.keyboardF.setOnClickListener {
            writeChar("F")
        }
        binding.keyboardG.setOnClickListener {
            writeChar("G")
        }
        binding.keyboardH.setOnClickListener {
            writeChar("H")
        }
        binding.keyboardI.setOnClickListener {
            writeChar("I")
        }
        binding.keyboardJ.setOnClickListener {
            writeChar("J")
        }
        binding.keyboardK.setOnClickListener {
            writeChar("K")
        }
        binding.keyboardL.setOnClickListener {
            writeChar("L")
        }
        binding.keyboardM.setOnClickListener {
            writeChar("M")
        }
        binding.keyboardN.setOnClickListener {
            writeChar("N")
        }
        binding.keyboardO.setOnClickListener {
            writeChar("O")
        }
        binding.keyboardP.setOnClickListener {
            writeChar("P")
        }
        binding.keyboardQ.setOnClickListener {
            writeChar("Q")
        }
        binding.keyboardR.setOnClickListener {
            writeChar("R")
        }
        binding.keyboardS.setOnClickListener {
            writeChar("S")
        }
        binding.keyboardT.setOnClickListener {
            writeChar("T")
        }
        binding.keyboardU.setOnClickListener {
            writeChar("U")
        }
        binding.keyboardV.setOnClickListener {
            writeChar("V")
        }
        binding.keyboardW.setOnClickListener {
            writeChar("W")
        }
        binding.keyboardX.setOnClickListener {
            writeChar("X")
        }
        binding.keyboardY.setOnClickListener {
            writeChar("Y")
        }
        binding.keyboardZ.setOnClickListener {
            writeChar("Z")
        }
        binding.keyboardSpace.setOnClickListener {
            writeChar(" ")
        }

        binding.keyboardBackspace.setOnClickListener {
            if (binding.countryTextField.text.isNotEmpty())
                binding.countryTextField.text.delete(
                    binding.countryTextField.text.length - 1,
                    binding.countryTextField.text.length
                )
        }
        binding.keyboardClear.setOnClickListener {
            binding.countryTextField.text.clear()
        }
    }*/

    /**
     * Appends the input character to the text box display of the input.
     *
     * @param[char] single character input of the key pressed.
     */
    fun writeChar(char: String) {
        binding.countryTextField.append(char)
    }
}