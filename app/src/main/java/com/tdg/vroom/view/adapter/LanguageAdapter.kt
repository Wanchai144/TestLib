package com.tdg.vroom.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tdg.vroom.base.RecyclerviewAdapterDiffCallback
import com.tdg.vroom.data.model.LanguageModel
import com.tdg.vroom.databinding.ItemLanguageViewBinding
import com.tdg.vroom.ext.gone
import com.tdg.vroom.ext.setOnClickWithDebounce
import com.tdg.vroom.ext.visible

class LanguageAdapter :
    ListAdapter<LanguageModel, LanguageAdapter.AdapterViewHolder>(
        RecyclerviewAdapterDiffCallback<LanguageModel>()
    ) {

    var onItemClick: ((LanguageModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        val binding =
            ItemLanguageViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        holder.bind(this.currentList, position)
    }

    override fun getItemCount(): Int {
        return this.currentList.size
    }

    inner class AdapterViewHolder(
        private val binding: ItemLanguageViewBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(dataResponse: MutableList<LanguageModel>, position: Int) {
            binding.apply {
                ivLanguage.setImageResource(dataResponse[position].icon)
                if (dataResponse[position].active) {
                    ivLanguageUnActive.gone()
                } else {
                    ivLanguageUnActive.visible()
                }
            }
            binding.root.setOnClickWithDebounce {
                val lastPosition = dataResponse.indexOfLast { it.active }
                if (lastPosition >= 0) {
                    dataResponse[lastPosition].active = false
                    notifyItemChanged(lastPosition)
                }
                dataResponse[position].active = true
                onItemClick?.invoke(dataResponse[position])
                notifyItemChanged(position)
            }
        }
    }
}