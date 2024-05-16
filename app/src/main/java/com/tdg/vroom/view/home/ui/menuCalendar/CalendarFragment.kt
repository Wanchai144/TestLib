package com.tdg.vroom.view.home.ui.menuCalendar

import android.provider.CalendarContract
import android.widget.Toast
import androidx.core.database.getIntOrNull
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import androidx.fragment.app.viewModels
import com.tdg.vroom.R
import com.tdg.vroom.base.BaseFragment
import com.tdg.vroom.data.local.room.entity.CalendarEntity
import com.tdg.vroom.databinding.FragmentCalendarBinding
import com.tdg.vroom.ext.DateTimeFormatExt
import com.tdg.vroom.ext.getDateFromTimeStamp
import com.tdg.vroom.ext.parseDateToddMMyyyy
import com.tdg.vroom.ext.startIntentMeetConferenceJitsi
import com.tdg.vroom.ext.startIntentMeetConferenceLiveKit
import com.tdg.vroom.utils.CalendarEvent
import com.tdg.vroom.utils.MyLog
import com.tdg.vroom.view.adapter.RecentAdapter
import com.tdg.vroom.view.home.HomeVIewModel
import com.tdg.vroom.view.home.ui.menuRecent.RecentViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class CalendarFragment : BaseFragment<FragmentCalendarBinding>() {

    private val calendarViewModel: CalendarViewModel by viewModels()

    private val recentAdapter: RecentAdapter by lazy { RecentAdapter() }

    private val recentViewModel: RecentViewModel by viewModels()

    private val homeViewModel: HomeVIewModel by viewModels()

    @Inject
    lateinit var calendarEvent: CalendarEvent

    override fun initView() {
        initListRecent()
        calendarEvent.getCalendarEvent(
            context = requireActivity(),
            calendarViewModel = calendarViewModel
        )
    }


    private fun initListRecent() = with(binding) {
        rvCalendar.apply {
            adapter = recentAdapter
        }
    }

    override fun onClickListener() = with(binding) {
        recentAdapter.onItemClick = { response, actionClickAdd ->
            if (actionClickAdd) {
               dialogMessageButton(
                   title = resources.getString(R.string.dialog_title),
                   message = resources.getString(R.string.message_add_event)
               ) {
                   calendarViewModel.saveCalendar(
                       param = CalendarEntity(
                           id = null, response.eventId, response.roomName,
                           "", "", response.time, "", "",
                           false, 0, "", 0,
                           false, "", false
                       ),
                   )
               }
            } else {
                homeViewModel.requestCheckVersionMeeting(roomName = response.roomName)
            }
        }
    }

    override fun initViewModel() {
        observeHistory()
        observeVersionMeetConference()
    }

    private fun observeVersionMeetConference() {
        homeViewModel.navigateLiveKit.observe(this) {
            recentViewModel.saveHistory(eventName = it?.second ?: "")
            if (it?.first == true) {
                requireActivity().startIntentMeetConferenceLiveKit(roomName = it.second)
            } else {
                requireActivity().startIntentMeetConferenceJitsi(roomName = it?.second ?: "")
            }
        }
    }

    private fun observeHistory() = with(calendarViewModel) {
        listCalendar.observe(viewLifecycleOwner) {
            recentAdapter.submitList(it)
        }
    }
}