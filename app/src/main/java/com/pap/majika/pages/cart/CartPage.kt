package com.pap.majika.pages.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.pap.majika.R
import com.pap.majika.pages.menu.MenuItemAdapter
import com.pap.majika.viewModel.MenuViewModel

class CartPage : Fragment() {
    private lateinit var viewModel: MenuViewModel
    private lateinit var recyclerView: androidx.recyclerview.widget.RecyclerView
    private lateinit var subtotal: android.widget.TextView
    private lateinit var payButton : android.widget.Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            requireActivity(),
            MenuViewModel.FACTORY
        )[MenuViewModel::class.java]
        viewModel.cartList.observe(this, androidx.lifecycle.Observer { tuple ->
            if (tuple !== null) {
                val cartItems = tuple.filter { it.value.quantity > 0 }
                if (cartItems.isNotEmpty()) {
                    recyclerView.adapter = MenuItemAdapter(cartItems, viewModel)
                    val subtotalString = tuple.keys.first().currency + " " + tuple.map { it.key.price * it.value.quantity }.sum().toString()
                    subtotal.text = subtotalString
                } else {
                    recyclerView.adapter = MenuItemAdapter(mapOf(), viewModel)
                    subtotal.text = "0.00"
                }
            } else {
                viewModel.refreshMenuList()
                recyclerView.adapter = MenuItemAdapter(mapOf(), viewModel)
                subtotal.text = "0.00"
            }
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart_page, container, false)
        recyclerView = view.findViewById(R.id.cart_recycler_view)
        subtotal = view.findViewById(R.id.cart_subtotal)
        payButton = view.findViewById(R.id.cart_pay_button)

        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        recyclerView.adapter = MenuItemAdapter(mapOf(), viewModel)

        return view
    }
}