package com.pap.majika.pages.cart

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.pap.majika.R
import com.pap.majika.activity.PaymentActivity
import com.pap.majika.pages.menu.MenuItemAdapter
import com.pap.majika.viewModel.MenuViewModel
import java.text.NumberFormat
import java.util.*

class CartPage : Fragment() {
    private lateinit var viewModel: MenuViewModel
    private lateinit var recyclerView: androidx.recyclerview.widget.RecyclerView
    private lateinit var subtotal: android.widget.TextView
    private lateinit var payButton : android.widget.Button
    private lateinit var errorText : android.widget.TextView
    private val formatPrice: NumberFormat = NumberFormat.getCurrencyInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            requireActivity(),
            MenuViewModel.FACTORY
        )[MenuViewModel::class.java]
        formatPrice.currency = Currency.getInstance(
            viewModel.cartList.value?.keys?.firstOrNull()?.currency ?: "IDR"
        )
        formatPrice.maximumFractionDigits = 2
        viewModel.cartList.observe(this) { tuple ->
            if (tuple !== null) {
                errorText.isVisible = false
                if (tuple.isNotEmpty()) {
                    recyclerView.adapter = MenuItemAdapter(tuple, viewModel)
                    val subtotalString = formatPrice.format(tuple.map {
                        it.key.price * it.value.quantity
                    }.sum())
                    subtotal.text = subtotalString
                    payButton.isVisible = true
                } else {
                    recyclerView.adapter = MenuItemAdapter(mapOf(), viewModel)
                    subtotal.text = "Cart is empty"
                    payButton.isVisible = false
                }
            } else {
                viewModel.refreshMenuList()
                recyclerView.adapter = MenuItemAdapter(mapOf(), viewModel)
                subtotal.text = ""
                payButton.isVisible = false
                errorText.isVisible = true
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart_page, container, false)
        recyclerView = view.findViewById(R.id.cart_recycler_view)
        subtotal = view.findViewById(R.id.cart_subtotal)
        payButton = view.findViewById(R.id.cart_pay_button)
        errorText = view.findViewById(R.id.cart_error_text)

        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        recyclerView.adapter = MenuItemAdapter(mapOf(), viewModel)

        payButton.setOnClickListener {
            startActivity(
                Intent(
                    this.requireContext(),
                    PaymentActivity::class.java,
                ).putExtra("total_price", subtotal.text)
            )
        }

        return view
    }
}