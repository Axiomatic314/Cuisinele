package com.example.cuisinele

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cuisinele.databinding.TutorialPageBinding

/**
 * Fragment class for the tutorial page.
 *
 * This class is used to display the rules of the game.
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
            findNavController().navigate(R.id.LoadingPage)
        }

        return binding.root
    }

    /**
     * This method ensures that the view is fully created.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    /**
     * This method destroys the view and unsets the binding variable.
     *
     * Also responsible for recording whether or not this was the first time the user opened the app.
     */
    override fun onDestroyView() {
        if (MainActivity.prefs != null && !MainActivity.prefs!!.getBoolean("isNotFirstLoad", false)) {
            val prefEdit = MainActivity.prefs!!.edit()
            prefEdit.apply {
                putBoolean("isNotFirstLoad", true)
                apply()
            }
        }
        super.onDestroyView()
        _binding = null
    }

}