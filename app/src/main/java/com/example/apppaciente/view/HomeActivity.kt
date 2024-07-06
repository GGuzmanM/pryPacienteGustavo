package com.example.apppaciente.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.apppaciente.R
import com.example.apppaciente.databinding.ActivityHomeBinding
import com.google.android.material.navigation.NavigationView

class HomeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setSupportActionBar(binding.appBarHome.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment)

        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val nombreUsuario = sharedPreferences.getString("nombreUsuario", "Jane Doe")
        val username = sharedPreferences.getString("username", "Jane Doe")
        val correoUsuario = sharedPreferences.getString("email", "jane.doe@example.com")
        val fotoUsuario = sharedPreferences.getString("foto", "")
        val apellido = sharedPreferences.getString("ape_completo", "Doe")
        val headerView = navView.getHeaderView(0)
        val navHeaderTitle: TextView = headerView.findViewById(R.id.nav_header_title)
        val navHeaderSubtitle: TextView = headerView.findViewById(R.id.nav_header_subtitle)
        //val navHeaderImageView: ImageView = headerView.findViewById(R.id.imageView)

        navHeaderTitle.text = nombreUsuario +" "+ apellido
        navHeaderSubtitle.text = correoUsuario

        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_home, R.id.nav_cita, R.id.nav_pagos), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_cita, R.id.nav_pagos
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_home_drawer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }



}
