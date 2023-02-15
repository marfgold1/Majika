package com.pap.majika.components

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import com.pap.majika.viewModel.MainViewModel
import com.pap.majika.R


class BottomNav : Fragment() {
    private var buttons: HashMap<String, Button> = HashMap()

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        mainViewModel.currentMenu.observe(this) {
            buttons.forEach { (key, value) ->
                if (key == it) {
                    value.compoundDrawables[1].setTint(resources.getColor(R.color.black))
                    value.setTextColor(resources.getColor(R.color.black))
                    value.setBackgroundColor(resources.getColor(R.color.white))
                } else {
                    value.compoundDrawables[1].setTint(resources.getColor(R.color.white))
                    value.setTextColor(resources.getColor(R.color.white))
                    value.setBackgroundColor(resources.getColor(com.google.android.material.R.color.design_default_color_primary))
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_bottom_nav, container, false)

        buttons["twibbon"] = view.findViewById(R.id.nav_button_twibbon) as Button
        buttons["branch"] = view.findViewById(R.id.nav_button_branch) as Button
        buttons["menu"] = view.findViewById(R.id.nav_button_menu) as Button
        buttons["cart"] = view.findViewById(R.id.nav_button_cart) as Button

        buttons["twibbon"]?.setOnClickListener {
            mainViewModel.currentMenu.value = "twibbon"
        }

        buttons["branch"]?.setOnClickListener {
            mainViewModel.currentMenu.value = "branch"
        }
        buttons["menu"]?.setOnClickListener {
            mainViewModel.currentMenu.value = "menu"
        }
        buttons["cart"]?.setOnClickListener {
            mainViewModel.currentMenu.value = "cart"
        }

        return view
    }
}