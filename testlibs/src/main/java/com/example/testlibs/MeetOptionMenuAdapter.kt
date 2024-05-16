package com.example.testlibs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.testlibs.databinding.ItemMeetOptionMenuViewBinding
import com.example.testlibs.persentation.adapter.RecyclerviewAdapterDiffCallback
import com.example.testlibs.persentation.ui.MeetOptionMenuModel
import com.example.testlibs.utils.setOnClickWithDebounce

enum class OPTION_MEET_CLICK(val menuName: String) {
    MENU_SHARE_SCREEN(menuName = "Share your screen"),
    MENU_RAISE_HAND(menuName = "Raise your hand"),
//    MENU_SOUND(menuName = "sound audio"),
//    MENU_PERMISSION(menuName = "permissions"),
//    MENU_DEBUG(menuName = "debug menu")
}

val menuName = arrayListOf(
    OPTION_MEET_CLICK.MENU_SHARE_SCREEN.menuName,
    OPTION_MEET_CLICK.MENU_RAISE_HAND.menuName,
//    OPTION_MEET_CLICK.MENU_PERMISSION.menuName,
//    OPTION_MEET_CLICK.MENU_DEBUG.menuName
)

val menuIcon = arrayListOf(
    R.drawable.baseline_cast_24,
    R.drawable.hand
//    R.drawable.volume_up_48px,
//    R.drawable.account_cancel_outline,
//    R.drawable.dots_horizontal_circle_outline
)

class MeetOptionMenuAdapter :
    ListAdapter<MeetOptionMenuModel, MeetOptionMenuAdapter.AdapterViewHolder>(
        RecyclerviewAdapterDiffCallback<MeetOptionMenuModel>()
    ) {

    var onItemClick: ((MeetOptionMenuModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        val binding =
            ItemMeetOptionMenuViewBinding.inflate(
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
        private val binding: ItemMeetOptionMenuViewBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(dataResponse: MutableList<MeetOptionMenuModel>, position: Int) {
            binding.apply {
                if (dataResponse[position].menuName == OPTION_MEET_CLICK.MENU_SHARE_SCREEN.menuName) {
                    ivIconMenu.setImageResource(if (dataResponse[position].stateShareScreen) {
                        R.drawable.baseline_cast_connected_24
                    } else {
                        R.drawable.baseline_cast_24
                    })
                } else {
                    ivIconMenu.setImageResource(dataResponse[position].icon)
                }
                tvMenuName.text = dataResponse[position].menuName
            }
            binding.root.setOnClickWithDebounce {
                onItemClick?.invoke(dataResponse[position])
            }
        }
    }
}