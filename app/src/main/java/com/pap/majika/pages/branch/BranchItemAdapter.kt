package com.pap.majika.pages.branch

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pap.majika.databinding.FragmentBranchItemBinding
import com.pap.majika.models.Branch

class BranchItemAdapter(
    private val branches: List<Branch>
) : RecyclerView.Adapter<BranchItemAdapter.BranchHolder>() {
    class BranchHolder(
        private val binding: FragmentBranchItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(branch: Branch) = with(binding) {
            addressText.text = branch.address
            nameText.text = branch.branch_name
            phoneText.text = branch.phone_number
            branchMapsBtn.setOnClickListener {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(
                        "http://maps.google.com/maps?q=loc:${branch.latitude},${branch.longitude} (${branch.branch_name})"
                    ),
                )
                val ctx = binding.root.context
                ctx.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BranchHolder {
        return BranchHolder(FragmentBranchItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))
    }

    override fun onBindViewHolder(holder: BranchHolder, position: Int) {
        holder.bind(branches[position])
    }

    override fun getItemCount(): Int {
        return branches.size
    }
}