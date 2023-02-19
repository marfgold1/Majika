package com.pap.majika.pages.branch

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pap.majika.R
import com.pap.majika.databinding.FragmentBranchItemBinding
import com.pap.majika.models.Branch

class BranchItemAdapter(
    private val branches: List<Branch>
) : RecyclerView.Adapter<BranchItemAdapter.BranchHolder>() {
    class BranchHolder(
        private val binding: FragmentBranchItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(branch: Branch) = with(binding) {
            val ctx = root.context
            addressText.text = branch.address
            nameText.text = branch.branch_name
            phoneText.text = ctx.getString(
                R.string.branch_phone_text,
                branch.phone_number,
                branch.contact_person,
            )
            popularFoodText.text = ctx.getString(
                R.string.branch_fav_food_text,
                branch.popular_food,
            )
            branchMapsBtn.setOnClickListener {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(
                        "http://maps.google.com/maps?q=loc:" +
                                "${branch.latitude},${branch.longitude} " +
                                "(${branch.branch_name})"
                    ),
                )
                ctx.startActivity(intent)
            }
            branchCallBtn.setOnClickListener {
                val intent = Intent(
                    Intent.ACTION_DIAL,
                    Uri.parse("tel:${branch.phone_number}"),
                )
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