package com.pap.majika.pages.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pap.majika.R
import com.pap.majika.models.Menu

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "name"
private const val ARG_PARAM2 = "description"
private const val ARG_PARAM3 = "currency"
private const val ARG_PARAM4 = "price"
private const val ARG_PARAM5 = "sold"
private const val ARG_PARAM6 = "type"

/**
 * A simple [Fragment] subclass.
 * Use the [MenuItem.newInstance] factory method to
 * create an instance of this fragment.
 */
class MenuItem : Fragment() {
    private var name: String? = null
    private var description: String? = null
    private var currency: String? = null
    private var price: Int? = null
    private var sold: Int? = null
    private var type: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            name = it.getString(ARG_PARAM1)
            description = it.getString(ARG_PARAM2)
            currency = it.getString(ARG_PARAM3)
            price = it.getInt(ARG_PARAM4)
            sold = it.getInt(ARG_PARAM5)
            type = it.getString(ARG_PARAM6)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu_item, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(menu: Menu) =
            MenuItem().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, menu.name)
                    putString(ARG_PARAM2, menu.description)
                    putString(ARG_PARAM3, menu.currency)
                    putInt(ARG_PARAM4, menu.price)
                    putInt(ARG_PARAM5, menu.sold)
                    putString(ARG_PARAM6, menu.type)
                }
            }
    }
}