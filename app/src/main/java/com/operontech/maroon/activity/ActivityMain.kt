package com.operontech.maroon.activity

import android.os.Bundle
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.Toast
import butterknife.BindView
import com.mapbox.mapboxsdk.Mapbox
import com.operontech.maroon.R
import com.operontech.maroon.frag.FragmentHome
import com.operontech.maroon.frag.FragmentMap
import com.operontech.maroon.frag.FragmentPlacesTypes

class ActivityMain : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var backButtonExitTriggered = false
    lateinit private var fragMap: FragmentMap

    @BindView(R.id.main_drawerLayout)
    lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up the main content (fragment)
        supportFragmentManager.beginTransaction().add(R.id.main_fragment, FragmentHome()).commit()

        setContentView(R.layout.activity_main)

        // Set up the toolbar
        val toolbar = findViewById(R.id.main_toolbar) as Toolbar
        setSupportActionBar(toolbar)

        // Set up the navigation drawer
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Set up navigation view listener
        val navigationView = findViewById(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)

        // Set up Mapbox
        Mapbox.getInstance(this@ActivityMain, getString(R.string.mapbox_access_token))

        // Don't slow down the main thread for the map (which is in the background)
        Thread(Runnable {
            // Create the map fragment to hide it in the background
            fragMap = FragmentMap()
            supportFragmentManager.beginTransaction().add(R.id.main_fragment, fragMap).hide(fragMap).commit()
        }).start()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else if (supportFragmentManager.backStackEntryCount == 0 && !backButtonExitTriggered) {
            backButtonExitTriggered = true
            Handler().postDelayed({ backButtonExitTriggered = false }, 2000)
            Toast.makeText(this, getString(R.string.back_button_exit), Toast.LENGTH_SHORT).show()
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Show the correct fragment based on item selected
        when (item.itemId) {
            R.id.nav_home -> showFragment(FragmentHome())
            R.id.nav_map -> showMapFragment()
            R.id.nav_places -> showFragment(FragmentPlacesTypes())
            R.id.nav_preferences -> TODO("Implement nav_preferences")
            R.id.nav_about -> TODO("Implement nav_about")
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.run {
            beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .hide(fragMap)
                    .commit()
            beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.main_fragment, fragment)
                    .commit()
        }
    }

    private fun showMapFragment() {
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.main_fragment, fragMap)
                .show(fragMap)
                .commit()
    }

    companion object {
        val MAROON_TAG = "Maroon"
    }
}