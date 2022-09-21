package com.example.cuisinele

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cuisinele.databinding.TutorialPageBinding

/**
 * Fragment class for the tutorial page.
 */
class Tutorial : Fragment() {

    private var _binding: TutorialPageBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    /**
     * Method creates and returns the view hierarchy associated with this fragment and inflates the page to be viewed.
     * @return the full xml page to be displayed.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = TutorialPageBinding.inflate(inflater, container, false)
        binding.continueBtn.setOnClickListener {
//            findNavController().navigate(R.id.Home)
            findNavController().navigate(R.id.LoadingPage)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    /**
     * Method destroys the view and unsets the binding variable.
     */
    override fun onDestroyView() {
        if (MainActivity.prefs != null && !MainActivity.prefs!!.getBoolean("isNotFirstLoad", false)) {
            var prefEdit = MainActivity.prefs!!.edit()
            prefEdit.apply {
                putBoolean("isNotFirstLoad", true)
                apply()
            }
        }
        super.onDestroyView()
        _binding = null
    }

}