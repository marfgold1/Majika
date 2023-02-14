package com.pap.majika

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.pap.majika.components.BottomNav

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

//    fun changePage(page: String) {
//        when (page) {
//            "twibbon" -> {
//                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, TwibbonPage()).commit()
//            }
//            "branch" -> {
//                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, BranchPage()).commit()
//            }
//            "menu" -> {
//                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, MenuPage()).commit()
//            }
//            "cart" -> {
//                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, CartPage()).commit()
//            }
//        }
//    }
}