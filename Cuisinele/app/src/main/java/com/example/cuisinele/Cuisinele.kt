package com.example.cuisinele

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cuisinele.databinding.CuisineleBinding

/**
 * @file
 * @brief A fragment class which acts as the main game page and the default navigation page.
 */
class Cuisinele : Fragment() {

    private var _binding: CuisineleBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    /**
     * @details Method creates and returns the view hierarchy associated with this fragment and calls the keyboard setup function.
     * @return the full xml page to be displayed.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CuisineleBinding.inflate(inflater, container, false)
        setKeyButtons()
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    /**
     * @details Method destorys the view and unsets the binding variable.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    /**
     * @brief Sets up the keyboard listeners.
     * @details Creates each individual button's listener with a call to the write method and sets up functionality for the clear and delete buttons.
     */
    fun setKeyButtons() {
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
                binding.countryTextField.text.delete(binding.countryTextField.text.length - 1, binding.countryTextField.text.length)
        }
        binding.keyboardClear.setOnClickListener {
            binding.countryTextField.text.clear()
        }
    }
    /**
     * @brief appends the input character to the text box display of the input.
     * @param single character input of the key pressed.
     */
    fun writeChar(char: String) {
        binding.countryTextField.append(char)
    }
}