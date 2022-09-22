package com.example.cuisinele

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.navigation.ui.onNavDestinationSelected
import com.example.cuisinele.databinding.ActivityMainBinding
/**
 * Class sets up the navigation bar and navigation between pages.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    companion object {
        var canGoBack: Boolean = true
        var prefs: SharedPreferences? = null
        private lateinit var binding: ActivityMainBinding
        /**
         * Function hides the top toolbar.
         */
        fun hideTopBar() {
            binding.toolbar.visibility = View.INVISIBLE
        }
        /**
         * Function reveals the top toolbar.
         */
        fun showTopBar() {
            binding.toolbar.visibility = View.VISIBLE
        }
    }

    /**
     * Creates the navigation bar.
     *
     * Method inflates the navigation bar and sets the view to the main game page.
     * Also sets up the bar with the navigation controller.
     *
     * @param[savedInstanceState] the state of the application.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.SuccessPage, R.id.FailurePage,
            R.id.Home, R.id.Tut, R.id.LoadingPage))

        setupActionBarWithNavController(navController, appBarConfiguration)

        prefs = getSharedPreferences("custom", MODE_PRIVATE)
        if (prefs != null) {
            val isNotFirstLoad = prefs!!.getBoolean("isNotFirstLoad", false)
            if (!isNotFirstLoad) {
                findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.Tut)
            }
        } else {
            findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.Tut)

        }
    }
    /**
     * Creates the icons for the navigation bar.
     *
     * Inflates the menu and adds the help button to the bar.
     * @param[menu] the main menu.
     *
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    /**
     * Handles clicks of items in the navigation bar.
     *
     * Attempts to navigate to the page associated with the item clicked.
     *
     * @param[item] the item clicked in the navigation.
     *
     * @return the page of the click.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }
    /**
     * Function handles upwards navigation from the action bar.
     *
     * @return true if it successfully navigates upwards.
     */
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
    /**
     * Handles clicks of the android back button to either go to the previous page or close the
     * app if no page is available.
     */
    override fun onBackPressed() {
        if (canGoBack) {
            findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.LoadingPage)
        } else {
            finish()
            System.out.close()
        }
    }
}