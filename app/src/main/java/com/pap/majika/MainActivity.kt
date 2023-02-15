package com.pap.majika

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.pap.majika.components.BottomNav

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var pageFragmentContainer: FragmentContainerView
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        setContentView(R.layout.activity_main)
        mainViewModel.currentMenu.observe(this, androidx.lifecycle.Observer {
            changePage(it)
        })
    }

    fun changePage(page: String) {
        when (page) {
            "twibbon" -> {
                this.findNavController(R.id.pageFragmentContainer).navigate(R.id.action_global_twibbonPage)
            }
            "cart" -> {
                this.findNavController(R.id.pageFragmentContainer).navigate(R.id.action_global_cartPage)
            }
            "menu" -> {
                this.findNavController(R.id.pageFragmentContainer).navigate(R.id.action_global_menuPage)
            }
            "branch" -> {
                this.findNavController(R.id.pageFragmentContainer).navigate(R.id.action_global_branchPage)
            }
        }
    }
}