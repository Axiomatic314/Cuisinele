package com.example.cuisinele

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cuisinele.data.ContextApplication
import com.example.cuisinele.data.CuisineleDAO
import com.example.cuisinele.data.CuisineleDB
import com.example.cuisinele.data.models.Dish
import java.time.LocalDate

class Loading : Fragment(R.layout.loading_page) {

    private lateinit var dao: CuisineleDAO
    private var dish: Dish? = null

    override fun onResume() {
        super.onResume()
        getData()
    }

    /**
     * Get the current days dish and uses it to check if the current game is completed
     * and navigates accordingly.
     */
    private fun getData() {
        dao = CuisineleDB.getInstance(ContextApplication.applicationContext()).cuisineleDAO()
        if (Settings.dailyGames) {
            // Convert current time since linux epoch from milliseconds to days
            var currentDate = LocalDate.now().toEpochDay()
            // The date we choose the dishes to start cycling from
            var cycleStartDate = Settings.startDate.toEpochDay()
            if (currentDate > cycleStartDate) {
                // calculate the day since the dish cycle begun and use modulus of the number of dishes to allow recycling of dishes
                var dishID: Int =
                    ((currentDate - cycleStartDate) % dao.getDishes().size).toInt()
                dish = dao.getDishByID(dishID)
            } else {
                // TODO: add message/exception for when the dish cycle hasn't begun (this should never occur)
            }
        }
        //if the daily cuisinele has already been completed, navigate to endscreen
        if (dish!!.IsComplete) {
            if (dish!!.GuessSix != 0) {
                if (dish!!.GuessSix == dish!!.CountryID) {
                    findNavController().navigate(R.id.SuccessPage)
                } else {
                    findNavController().navigate(R.id.FailurePage)
                }
            } else {
                findNavController().navigate(R.id.SuccessPage)
            }
        } else {
            findNavController().navigate(R.id.Home)
        }

    }
    /**
     * Method destroys the view and unsets the binding variable.
     */
    override fun onDestroyView() {
        super.onDestroyView()
    }
}