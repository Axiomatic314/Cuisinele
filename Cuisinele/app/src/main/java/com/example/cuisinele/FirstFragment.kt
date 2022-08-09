package com.example.cuisinele

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cuisinele.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        setKeyButtons();

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setKeyButtons() {
        binding.keyboardA.setOnClickListener {
            binding.countryTextField.text.append("A");
        }
        binding.keyboardB.setOnClickListener {
            binding.countryTextField.text.append("B");
        }
        binding.keyboardC.setOnClickListener {
            binding.countryTextField.text.append("C");
        }
        binding.keyboardD.setOnClickListener {
            binding.countryTextField.text.append("D");
        }
        binding.keyboardE.setOnClickListener {
            binding.countryTextField.text.append("E");
        }
        binding.keyboardF.setOnClickListener {
            binding.countryTextField.text.append("F");
        }
        binding.keyboardG.setOnClickListener {
            binding.countryTextField.text.append("G");
        }
        binding.keyboardH.setOnClickListener {
            binding.countryTextField.text.append("H");
        }
        binding.keyboardI.setOnClickListener {
            binding.countryTextField.text.append("I");
        }
        binding.keyboardJ.setOnClickListener {
            binding.countryTextField.text.append("J");
        }
        binding.keyboardK.setOnClickListener {
            binding.countryTextField.text.append("K");
        }
        binding.keyboardL.setOnClickListener {
            binding.countryTextField.text.append("L");
        }
        binding.keyboardM.setOnClickListener {
            binding.countryTextField.text.append("M");
        }
        binding.keyboardN.setOnClickListener {
            binding.countryTextField.text.append("N");
        }
        binding.keyboardO.setOnClickListener {
            binding.countryTextField.text.append("O");
        }
        binding.keyboardP.setOnClickListener {
            binding.countryTextField.text.append("P");
        }
        binding.keyboardQ.setOnClickListener {
            binding.countryTextField.text.append("Q");
        }
        binding.keyboardR.setOnClickListener {
            binding.countryTextField.text.append("R");
        }
        binding.keyboardS.setOnClickListener {
            binding.countryTextField.text.append("S");
        }
        binding.keyboardT.setOnClickListener {
            binding.countryTextField.text.append("T");
        }
        binding.keyboardU.setOnClickListener {
            binding.countryTextField.text.append("U");
        }
        binding.keyboardV.setOnClickListener {
            binding.countryTextField.text.append("V");
        }
        binding.keyboardW.setOnClickListener {
            binding.countryTextField.text.append("W");
        }
        binding.keyboardX.setOnClickListener {
            binding.countryTextField.text.append("X");
        }
        binding.keyboardY.setOnClickListener {
            binding.countryTextField.text.append("Y");
        }
        binding.keyboardZ.setOnClickListener {
            binding.countryTextField.text.append("Z");
        }
        binding.keyboardSpace.setOnClickListener {
            binding.countryTextField.text.append(" ");
        }
        binding.keyboardBackspace.setOnClickListener {
            if (binding.countryTextField.text.isNotEmpty())
                binding.countryTextField.text.delete(binding.countryTextField.text.length - 1, binding.countryTextField.text.length);
        }
        binding.keyboardClear.setOnClickListener {
            binding.countryTextField.text.clear();
        }
    }
}