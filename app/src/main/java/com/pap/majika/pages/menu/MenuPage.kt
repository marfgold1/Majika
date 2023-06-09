package com.pap.majika.pages.menu

import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.pap.majika.R
import com.pap.majika.databinding.FragmentMenuPageBinding
import com.pap.majika.viewModel.MenuViewModel

class MenuPage : Fragment() {
    private lateinit var viewModel: MenuViewModel

    private var _binding: FragmentMenuPageBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            requireActivity(),
            MenuViewModel.FACTORY
        )[MenuViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuPageBinding.inflate(inflater, container, false)
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.menu_types,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.menuFilter.adapter = adapter
        }
        binding.menuFilter.setSelection(0)
        binding.menuFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.filterMenuList(binding.menuSearch.text.toString(), binding.menuFilter.selectedItem.toString())
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.menuSearch.text.clear()
        binding.menuSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.filterMenuList(binding.menuSearch.text.toString(), binding.menuFilter.selectedItem.toString())
            }
            override fun afterTextChanged(s: android.text.Editable?) {}
        })

        binding.menuRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.menuRecyclerView.adapter = MenuItemAdapter(mapOf(), viewModel)

        binding.menuSwipeLayout.setOnRefreshListener {
            binding.menuRecyclerView.visibility = View.GONE
            binding.menuSearchLayout.visibility = View.GONE
            binding.errorLayout.visibility = View.GONE
            viewModel.refreshMenuList()
        }
        viewModel.menuList.observe(viewLifecycleOwner) { tuple ->
            Log.d("MenuPage", "Menu list updated with ${tuple?.size} items")
            if (tuple !== null) {
                if (tuple.isEmpty())
                    binding.errorLayout.visibility = View.VISIBLE
                else
                    binding.errorLayout.visibility = View.GONE
                binding.menuSwipeLayout.isRefreshing = false
                binding.menuRecyclerView.visibility = View.VISIBLE
                binding.menuSearchLayout.visibility = View.VISIBLE
                viewModel.filterMenuList(binding.menuSearch.text.toString(), binding.menuFilter.selectedItem.toString())
                binding.menuRecyclerView.adapter = MenuItemAdapter(tuple.toSortedMap(
                    compareBy {
                        it.name + it.description
                    }
                ), viewModel)
            } else {
                binding.menuRecyclerView.visibility = View.GONE
                binding.menuSearchLayout.visibility = View.GONE
                binding.errorLayout.visibility = View.GONE
            }
        }

        binding.errorRefreshButton.setOnClickListener {
            binding.menuRecyclerView.visibility = View.GONE
            binding.menuSearchLayout.visibility = View.GONE
            binding.errorLayout.visibility = View.GONE
            binding.menuSwipeLayout.isRefreshing = true
            viewModel.refreshMenuList()
        }
        binding.menuSwipeLayout.isRefreshing = true
        return binding.root
    }
}