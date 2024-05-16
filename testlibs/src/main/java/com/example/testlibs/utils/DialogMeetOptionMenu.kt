package com.example.testlibs.utils

import android.content.Context
import com.example.testlibs.MeetOptionMenuAdapter
import com.example.testlibs.R
import com.example.testlibs.databinding.DialogMeetOptionmenuBinding
import com.example.testlibs.persentation.ui.MeetOptionMenuModel
import com.lxj.xpopup.core.BottomPopupView

class DialogMeetOptionMenu (
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