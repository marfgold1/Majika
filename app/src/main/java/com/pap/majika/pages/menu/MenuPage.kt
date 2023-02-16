package com.pap.majika.pages.menu

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.pap.majika.R
import com.pap.majika.viewModel.MenuViewModel

class MenuPage : Fragment() {

    private lateinit var viewModel: MenuViewModel
    private lateinit var recyclerView: androidx.recyclerview.widget.RecyclerView
    private lateinit var spinner: android.widget.ProgressBar

    private lateinit var searchLayout : android.widget.LinearLayout
    private lateinit var search: android.widget.EditText
    private lateinit var filter: android.widget.Spinner
    private lateinit var errorText: android.widget.TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            requireActivity(),
            MenuViewModel.FACTORY
        )[MenuViewModel::class.java]
        viewModel.menuList.observe(this, androidx.lifecycle.Observer {
            if (it !== null) {
                if (spinner.visibility == View.VISIBLE) {
                    spinner.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    searchLayout.visibility = View.VISIBLE
                }
                recyclerView.adapter = MenuItemAdapter(it, viewModel)
                if (it.isEmpty()) {
                    errorText.visibility = View.VISIBLE
                } else {
                    errorText.visibility = View.GONE
                }
            } else {
                recyclerView.adapter = MenuItemAdapter(mapOf(), viewModel)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_menu_page, container, false)
        recyclerView = view.findViewById(R.id.menu_recycler_view)
        spinner = view.findViewById(R.id.menu_spinner)
        searchLayout = view.findViewById(R.id.menu_search_layout)
        search = view.findViewById(R.id.menu_search)
        filter = view.findViewById(R.id.menu_filter)
        errorText = view.findViewById(R.id.menu_error_text)


        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.menu_types,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            filter.adapter = adapter
        }
        filter.setSelection(0)
        filter.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: android.widget.AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.filterMenuList(search.text.toString(), filter.selectedItem.toString())
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {
            }
        }

        search.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.filterMenuList(search.text.toString(), filter.selectedItem.toString())
            }

            override fun afterTextChanged(s: android.text.Editable?) {
            }
        })


        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        val adapter = MenuItemAdapter(mapOf(), viewModel)
        recyclerView.adapter = adapter
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.refreshMenuList()
    }

}