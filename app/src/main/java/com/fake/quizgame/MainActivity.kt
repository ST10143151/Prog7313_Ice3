package com.fake.quizgame

import android.content.Intent
import android.util.Log
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        setupNavigationDrawer()

        // ✅ Hide home message when not on category screen
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val homeMessage = findViewById<TextView>(R.id.homeMessage)
            homeMessage?.visibility =
                if (destination.id == R.id.categoryFragment) TextView.VISIBLE else TextView.GONE
        }
    }

    private fun setupNavigationDrawer() {
        val headerView = navView.getHeaderView(0)
        val tvWelcome = headerView.findViewById<TextView>(R.id.tvWelcome)
        val currentUser = FirebaseAuth.getInstance().currentUser

        tvWelcome.text = currentUser?.displayName ?: "Welcome, Guest"

        val menu = navView.menu
        menu.findItem(R.id.nav_login).isVisible = currentUser == null
        menu.findItem(R.id.nav_register).isVisible = currentUser == null
        menu.findItem(R.id.nav_profile).isVisible = currentUser != null

        navView.setNavigationItemSelectedListener { menuItem ->
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController = navHostFragment.navController

            when (menuItem.itemId) {
                R.id.nav_login -> startActivity(Intent(this, LoginActivity::class.java))
                R.id.nav_register -> startActivity(Intent(this, RegistrationActivity::class.java))
                R.id.nav_profile -> startActivity(Intent(this, ProfileActivity::class.java))
                R.id.nav_quizzes -> navController.navigate(R.id.categoryFragment)
                R.id.nav_logout -> {
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }

            drawerLayout.closeDrawers()
            true
        }
    }
}
