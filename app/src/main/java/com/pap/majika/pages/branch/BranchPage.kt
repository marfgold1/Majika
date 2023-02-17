package com.pap.majika.pages.branch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.pap.majika.databinding.FragmentBranchPageBinding
import com.pap.majika.models.Branch
import com.pap.majika.models.Response
import com.pap.majika.viewModel.BranchViewModel
import kotlinx.coroutines.launch

class BranchPage : Fragment() {
    private var _binding: FragmentBranchPageBinding? = null
    private val binding get() = _binding!!
    val branchViewModel: BranchViewModel by activityViewModels()

    fun changeStatus(items: Response<List<Branch>>?) {
        with (binding) {
            if (items == null) {
                branchSwipeLayout.isRefreshing = true
                branchStatusLayout.isVisible = false
            } else if (items.data == null) {
                branchSwipeLayout.isRefreshing = false
                branchStatusLayout.isVisible = true
            } else {
                branchSwipeLayout.isRefreshing = false
                branchStatusLayout.isVisible = false
                branchView.adapter = BranchItemAdapter(items.data!!.sortedBy {
                    it.branch_name
                })
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentBranchPageBinding.inflate(inflater, container, false)
        binding.branchSwipeLayout.setOnRefreshListener {
            branchViewModel.getBranches()
        }
        binding.branchTryAgainBtn.setOnClickListener {
            branchViewModel.getBranches()
        }
        binding.branchView.layoutManager = LinearLayoutManager(context)
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                branchViewModel.uiState.collect {
                    changeStatus(it)
                }
            }
        }
        return binding.root
    }
}