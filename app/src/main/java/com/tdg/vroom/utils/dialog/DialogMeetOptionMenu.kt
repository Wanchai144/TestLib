package com.tdg.vroom.utils.dialog

import android.content.Context
import com.lxj.xpopup.core.BottomPopupView
import com.tdg.vroom.R
import com.tdg.vroom.data.model.MeetOptionMenuModel
import com.tdg.vroom.databinding.DialogMeetOptionmenuBinding
import com.tdg.vroom.view.adapter.MeetOptionMenuAdapter

class DialogMeetOptionMenu(
    private var context: Context,
    private var listMenu: ArrayList<MeetOptionMenuModel>,
    private var onClickCallback: (MeetOptionMenuModel) -> Unit
) : BottomPopupView(context) {

    private lateinit var binding: DialogMeetOptionmenuBinding

    private val meetOptionMenuAdapter: MeetOptionMenuAdapter by lazy {
        MeetOptionMenuAdapter()
    }

    override fun getImplLayoutId() = R.layout.dialog_meet_optionmenu

    override fun onCreate() {
        super.onCreate()

        binding = DialogMeetOptionmenuBinding.bind(popupImplView)
        setDetail()
    }

    private fun setDetail() = with(binding) {
        rvMeetMenu.apply {
            meetOptionMenuAdapter.submitList(listMenu)
            adapter = meetOptionMenuAdapter
        }

        meetOptionMenuAdapter.onItemClick = {
            onClickCallback.invoke(it)
            dialog.dismiss()
        }
    }
}