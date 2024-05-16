package com.tdg.vroom.view.home.ui.home

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.os.Build
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.tdg.vroom.R
import com.tdg.vroom.base.BaseFragment
import com.tdg.vroom.databinding.FragmentHomeBinding
import com.tdg.vroom.ext.gone
import com.tdg.vroom.ext.launchActivity
import com.tdg.vroom.ext.onTextChangeListener
import com.tdg.vroom.ext.setOnClickWithDebounce
import com.tdg.vroom.ext.startIntentMeetConferenceJitsi
import com.tdg.vroom.ext.startIntentMeetConferenceLiveKit
import com.tdg.vroom.ext.visible
import com.tdg.vroom.utils.keyBoardState.KeyboardStateEvent
import com.tdg.vroom.view.conferenceMeet.jitsi.JitsiMeetConferenceActivity
import com.tdg.vroom.view.conferenceMeet.livekit.LiveKitMeetConferenceActivity
import com.tdg.vroom.view.home.HomeVIewModel
import com.tdg.vroom.view.home.ui.menuRecent.RecentViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val homeViewModel: HomeVIewModel by viewModels()

    private val recentViewModel: RecentViewModel by viewModels()

    private var isSelectMenuRecent = true

    override fun initView() {
        setLoadingView(homeViewModel.loadingState)
        bindTextMenu()
        checkOnFocusEdt()
        onTextChangeListener()
    }

    private fun bindTextMenu() = with(binding) {
        clearInputRoomEmpty()
        if (isSelectMenuRecent) {
            viewMenuRecent.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
            viewMenuCalendar.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorHeaderGray
                )
            )
            viewContentRecent.visible()
            viewContentCalendar.gone()
        } else {
            viewMenuRecent.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorHeaderGray
                )
            )
            viewMenuCalendar.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
            viewContentRecent.gone()
            viewContentCalendar.visible()
        }
    }

    private fun checkOnFocusEdt() {
        KeyboardStateEvent.setEventListener(
            requireActivity()
        ) { isOpen ->
            if (isOpen) {
                binding.viewInfoJoinMeet.visible()
            } else {
                binding.viewInfoJoinMeet.gone()
            }
        }
    }

    private fun onTextChangeListener() = with(binding) {
        edtRoomName.onTextChangeListener {
            btnCreateJoin.isEnabled = it.isNotEmpty()
        }

        edtRoomName.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (binding.edtRoomName.text.toString().isNotEmpty()) {
                    setUpNavigateMeetingConference()
                }
                return@setOnEditorActionListener true
            }
            false
        }
    }

    private fun setUpNavigateMeetingConference() {
        homeViewModel.requestCheckVersionMeeting(roomName = binding.edtRoomName.text.toString())
    }

    override fun onClickListener() = with(binding) {
        btnCreateJoin.setOnClickWithDebounce {
            setUpNavigateMeetingConference()
        }

        viewMenuRecent.setOnClickWithDebounce {
            isSelectMenuRecent = true
            bindTextMenu()
        }

        viewMenuCalendar.setOnClickWithDebounce {
            isSelectMenuRecent = false
            bindTextMenu()
        }
    }

    private fun clearInputRoomEmpty() = with(binding) {
        edtRoomName.clearFocus()
        edtRoomName.setText("")
    }

    override fun initViewModel() {
        observeVersionMeetConference()
        observeOnError()
    }

    private fun observeOnError() {
        homeViewModel.onErrorMessage.observe(this) {
            dialogMessage(
                title = resources.getString(R.string.dialog_title),
                message = it ?: ""
            ) {}
        }
    }

    private fun observeVersionMeetConference() {
        homeViewModel.navigateLiveKit.observe(this) {
            recentViewModel.saveHistory(eventName = binding.edtRoomName.text.toString())
            if (it?.first == true) {
                requireActivity().startIntentMeetConferenceLiveKit(roomName = binding.edtRoomName.text.toString())
            } else {
                requireActivity().startIntentMeetConferenceJitsi(roomName = binding.edtRoomName.text.toString())
            }
            clearInputRoomEmpty()
        }
    }
}