package com.pap.majika.pages.menu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pap.majika.databinding.FragmentMenuItemBinding
import com.pap.majika.models.CartItem
import com.pap.majika.models.Menu
import com.pap.majika.viewModel.MenuViewModel

class MenuItemAdapter(
    private val menu: Map<Menu, CartItem>,
    private val menuViewModel: MenuViewModel
    ) : RecyclerView.Adapter<MenuItemAdapter.ViewHolder>()
{
    class ViewHolder(
        private val binding: FragmentMenuItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(menu: Menu, cart: CartItem, menuViewModel: MenuViewModel) = with(binding) {
            menuCartQty.text = cart.quantity.toString()
            menuItemName.text = menu.name
            menuItemDescription.text = menu.description
            menuItemCurrency.text = menu.currency
            menuItemPrice.text = menu.price.toString()
            menuItemSold.text = "Sold: ${menu.sold}"
            menuAddToCart.setOnClickListener {
                menuViewModel.addToCart(menu)
                if (cart.quantity == 1) menuRemoveFromCart.isClickable = true
                menuCartQty.text = cart.quantity.toString()
            }
            menuRemoveFromCart.setOnClickListener {
                menuViewModel.removeFromCart(menu)
                if (cart.quantity == 0) menuRemoveFromCart.isClickable = false
                menuCartQty.text = cart.quantity.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FragmentMenuItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))
    }

    override fun getItemCount(): Int {
        return menu.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = menu.keys.elementAt(position)
        holder.bind(item, menu[item]!!, menuViewModel)
    }
}