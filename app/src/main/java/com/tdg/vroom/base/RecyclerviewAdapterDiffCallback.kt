package com.tdg.vroom.base

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

class RecyclerviewAdapterDiffCallback<T : Any> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }

}
