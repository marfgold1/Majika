package com.pap.majika.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.pap.majika.R
import com.pap.majika.viewModel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        setContentView(R.layout.activity_main)
        mainViewModel.currentMenu.observe(this, androidx.lifecycle.Observer {
            changePage(it)
        })
    }

    override fun onBackPressed() {
        val temp = super.onBackPressed()
        navController = this.findNavController(R.id.pageFragmentContainer)
        when (navController.currentDestination?.id) {
            R.id.twibbonPage -> {
                mainViewModel.currentMenu.value = "twibbon"
            }
            R.id.cartPage -> {
                mainViewModel.currentMenu.value = "cart"
            }
            R.id.menuPage -> {
                mainViewModel.currentMenu.value = "menu"
            }
            R.id.branchPage -> {
                mainViewModel.currentMenu.value = "branch"
            }
        }
        return temp
    }

    private fun changePage(page: String) {
        navController = this.findNavController(R.id.pageFragmentContainer)
        when (page) {
            "twibbon" -> {
                navController.navigate(R.id.action_global_twibbonPage)
            }
            "cart" -> {
                navController.navigate(R.id.action_global_cartPage)
            }
            "menu" -> {
                navController.navigate(R.id.action_global_menuPage)
            }
            "branch" -> {
                navController.navigate(R.id.action_global_branchPage)
            }
        }
    }
}