package ru.practicum.android.diploma.ui.root

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.practicum.android.diploma.R

class RootActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val bottomNavDivider = findViewById<View>(R.id.bottomNavigationDivider)
        bottomNav.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.vacancyFragment,
                R.id.filterSettingsFragment,
                R.id.selectRegionFragment,
                R.id.selectCountryFragment,
                R.id.selectIndustryFragment,
                R.id.workPlaceFragment -> {
                    showBottomNav(bottomNav, bottomNavDivider, false)
                }

                else -> {
                    showBottomNav(bottomNav, bottomNavDivider, true)
                }
            }

        }
    }

    private fun showBottomNav(bottomNavMenu: View, bottomNavMenuTopDividerLine: View, show: Boolean) {
        if (!show) {
            bottomNavMenu.isGone = true
            bottomNavMenuTopDividerLine.isGone = true
        } else {
            bottomNavMenu.isVisible = true
            bottomNavMenuTopDividerLine.isVisible = true
        }
    }
}
