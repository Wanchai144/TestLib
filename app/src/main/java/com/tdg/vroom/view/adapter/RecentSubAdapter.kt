package com.tdg.vroom.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tdg.vroom.R
import com.tdg.vroom.base.RecyclerviewAdapterDiffCallback
import com.tdg.vroom.data.model.RecentModel
import com.tdg.vroom.databinding.ItemSubheaderRecentViewBinding
import com.tdg.vroom.ext.getTextTwoCharacter
import com.tdg.vroom.ext.gone
import com.tdg.vroom.ext.setOnClickWithDebounce
import com.tdg.vroom.ext.visible

class RecentSubAdapter :
    ListAdapter<RecentModel.SubRecentModel, RecentSubAdapter.AdapterViewHolder>(
        RecyclerviewAdapterDiffCallback<RecentModel.SubRecentModel>()
    ) {

    var onSubItemClick: ((RecentModel.SubRecentModel, Boolean) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        val binding =
            ItemSubheaderRecentViewBinding.inflate(
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
        private val binding: ItemSubheaderRecentViewBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(dataResponse: MutableList<RecentModel.SubRecentModel>, position: Int) {
            binding.apply {
                tvImageName.text = dataResponse[position].txtImage
                tvRoomName.text = dataResponse[position].roomName
                tvDescription.text = dataResponse[position].description
                tvTime.text = dataResponse[position].time
                tvImageName.background = ContextCompat.getDrawable(
                    binding.root.context,
                    dataResponse[position].backgroundColor
                )

                if (dataResponse[position].stateViewAddMeeting){
                    ivAddMeeting.visible()
                    tvDescription.gone()
                }else{
                    tvDescription.visible()
                    ivAddMeeting.gone()
                }

                ivAddMeeting.setOnClickListener {
                    onSubItemClick?.invoke(dataResponse[position],true)
                }

                root.setOnClickWithDebounce {
                    if (dataResponse[position].stateViewAddMeeting.not()){
                        onSubItemClick?.invoke(dataResponse[position],false)
                    }
                }
            }
        }
    }
}