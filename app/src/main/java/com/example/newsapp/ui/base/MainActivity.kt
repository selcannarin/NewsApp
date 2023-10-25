package com.example.newsapp.ui.base

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.newsapp.R
import com.example.newsapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        if (!isNetworkConnected()) {
            showNoInternetDialog()
        } else {
            setContentView(binding.root)
            setupNavigation()
            setBackButton()
        }
    }

    private fun isNetworkConnected(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun showNoInternetDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("No Internet Connection")
        alertDialogBuilder.setMessage("Please check your internet connection.")
        alertDialogBuilder.setNegativeButton("CLOSE APPLICATION") { _, _ ->
            finish()
        }
        alertDialogBuilder.setCancelable(false)
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun setupNavigation() {
        val navController = findNavController(R.id.fragmentContainerView)

        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.news -> {
                    navController.navigate(R.id.newsFragment)
                    return@setOnItemSelectedListener true
                }

                R.id.favorites -> {
                    navController.navigate(R.id.favoritesFragment)
                    return@setOnItemSelectedListener true
                }

                else -> {
                    return@setOnItemSelectedListener false
                }
            }
        }
    }

    fun setToolbarTitle(title: String) {
        binding.toolbarTitle.text = title
    }
    fun setToolbarVisibilityGONE() {
        binding.toolbar.visibility = View.GONE
    }
    fun setToolbarVisibilityVISIBLE() {
        binding.toolbar.visibility = View.VISIBLE
    }

    fun setBackButton(){
        binding.ivBackButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}