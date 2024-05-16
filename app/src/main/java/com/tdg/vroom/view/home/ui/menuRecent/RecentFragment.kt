package com.tdg.vroom.view.home.ui.menuRecent

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.tdg.vroom.R
import com.tdg.vroom.base.BaseFragment
import com.tdg.vroom.databinding.FragmentRecentBinding
import com.tdg.vroom.ext.launchActivity
import com.tdg.vroom.ext.startIntentMeetConferenceJitsi
import com.tdg.vroom.ext.startIntentMeetConferenceLiveKit
import com.tdg.vroom.view.adapter.RecentAdapter
import com.tdg.vroom.view.conferenceMeet.jitsi.JitsiMeetConferenceActivity
import com.tdg.vroom.view.conferenceMeet.livekit.LiveKitMeetConferenceActivity
import com.tdg.vroom.view.home.HomeVIewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RecentFragment : BaseFragment<FragmentRecentBinding>() {

    private val recentViewModel: RecentViewModel by viewModels()

    private val homeViewModel: HomeVIewModel by viewModels()

    private val recentAdapter: RecentAdapter by lazy { RecentAdapter() }

    override fun initView() {
        setLoadingView(homeViewModel.loadingState)
        initListRecent()
    }

    private fun initListRecent() = with(binding) {
        rvRecent.apply {
            adapter = recentAdapter
        }
    }

    override fun onClickListener() = with(binding) {
        recentAdapter.onItemClick = { response, actionClickAdd ->
            if (actionClickAdd) {
                Toast.makeText(requireContext(), "add to calendar", Toast.LENGTH_SHORT).show()
            } else {
                homeViewModel.requestCheckVersionMeeting(roomName = response.roomName)
            }
        }
    }

    override fun initViewModel() {
        observeHistory()
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

    private fun observeHistory() = with(recentViewModel) {
        listRecent.observe(viewLifecycleOwner) {
            recentAdapter.submitList(it)
        }
    }

    private fun observeVersionMeetConference() {
        homeViewModel.navigateLiveKit.observe(this) {
            if (it?.first == true) {
                requireActivity().startIntentMeetConferenceLiveKit(roomName = it.second)
            } else {
                requireActivity().startIntentMeetConferenceJitsi(roomName = it?.second ?: "")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        recentViewModel.requestHistory()
    }
}