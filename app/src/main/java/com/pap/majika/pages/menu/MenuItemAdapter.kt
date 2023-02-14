package com.pap.majika.pages.menu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pap.majika.R
import com.pap.majika.models.Menu

class MenuItemAdapter(private val menu: List<Menu>) : RecyclerView.Adapter<MenuItemAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView
        val description: TextView
        val currency: TextView
        val price: TextView
        val sold: TextView

        init {
            name = view.findViewById(R.id.menu_item_name)
            description = view.findViewById(R.id.menu_item_description)
            currency = view.findViewById(R.id.menu_item_currency)
            price = view.findViewById(R.id.menu_item_price)
            sold = view.findViewById(R.id.menu_item_sold)
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
        val item = menu[position]
        holder.name.text = item.name
        holder.description.text = item.description
        holder.currency.text = item.currency
        holder.price.text = item.price.toString()
        holder.sold.text = "Sold: ${item.sold}"
    }
}