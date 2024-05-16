package com.tdg.vroom.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tdg.vroom.base.RecyclerviewAdapterDiffCallback
import com.tdg.vroom.data.model.RecentModel
import com.tdg.vroom.databinding.ItemHeaderRecentViewBinding

class RecentAdapter() :
    ListAdapter<RecentModel, RecentAdapter.AdapterViewHolder>(
        RecyclerviewAdapterDiffCallback<RecentModel>()
    ) {

    var onItemClick: ((RecentModel.SubRecentModel, Boolean) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        val binding =
            ItemHeaderRecentViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return AdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        holder.bind(this.currentList, position)
    }

    override fun getItemCount(): Int {
        return this.currentList.size
    }

    inner class AdapterViewHolder(
        private val binding: ItemHeaderRecentViewBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(dataResponse: MutableList<RecentModel>, position: Int) {
            val subRecentAdapter = RecentSubAdapter()
            binding.apply {
                tvTitle.text = dataResponse[position].title
                rvListRecent.apply {
                    adapter = subRecentAdapter
                }
                subRecentAdapter.submitList(dataResponse[position].dataMeet)
            }

            subRecentAdapter.onSubItemClick = { response, actionClickAdd ->
                onItemClick?.invoke(response, actionClickAdd)
            }
        }
    }
}