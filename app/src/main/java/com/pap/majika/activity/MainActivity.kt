package com.pap.majika.activity

import android.os.Bundle
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