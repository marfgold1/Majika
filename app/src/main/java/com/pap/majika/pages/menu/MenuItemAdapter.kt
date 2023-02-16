package com.pap.majika.pages.menu

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.pap.majika.MajikaApp
import com.pap.majika.R
import com.pap.majika.models.CartItem
import com.pap.majika.models.Menu
import com.pap.majika.repository.AppRepository
import com.pap.majika.viewModel.MenuViewModel

class MenuItemAdapter(private val menu: Map<Menu, CartItem>, private val menuViewModel: MenuViewModel) : RecyclerView.Adapter<MenuItemAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView
        val description: TextView
        val currency: TextView
        val price: TextView
        val sold: TextView
        val addToCart: ImageButton
        val removeFromCart: ImageButton
        val cartQty: TextView
        var cartItem: CartItem? = null

        init {
            name = view.findViewById(R.id.menu_item_name)
            description = view.findViewById(R.id.menu_item_description)
            currency = view.findViewById(R.id.menu_item_currency)
            price = view.findViewById(R.id.menu_item_price)
            sold = view.findViewById(R.id.menu_item_sold)
            addToCart = view.findViewById(R.id.menu_add_to_cart)
            removeFromCart = view.findViewById(R.id.menu_remove_from_cart)
            cartQty = view.findViewById(R.id.menu_cart_qty)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_menu_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return menu.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = menu.keys.elementAt(position)
        holder.cartItem = menu[item]
        holder.cartQty.text = holder.cartItem!!.quantity.toString()
        holder.name.text = item.name
        holder.description.text = item.description
        holder.currency.text = item.currency
        holder.price.text = item.price.toString()
        holder.sold.text = "Sold: ${item.sold}"
        holder.addToCart.setOnClickListener {
            menuViewModel.addToCart(item)
            if (holder.cartItem!!.quantity == 1) {
                holder.removeFromCart.isClickable = true
            }
            holder.cartQty.text = holder.cartItem!!.quantity.toString()

        }
        holder.removeFromCart.setOnClickListener {
            menuViewModel.removeFromCart(item)
            if (holder.cartItem!!.quantity == 0) {
                holder.removeFromCart.isClickable = false
            }
            holder.cartQty.text = holder.cartItem!!.quantity.toString()
        }
    }
}