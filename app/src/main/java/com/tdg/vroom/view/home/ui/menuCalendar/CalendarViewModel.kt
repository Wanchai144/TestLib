package com.tdg.vroom.view.home.ui.menuCalendar

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tdg.vroom.R
import com.tdg.vroom.base.BaseViewModel
import com.tdg.vroom.data.local.room.entity.CalendarEntity
import com.tdg.vroom.data.model.RecentModel
import com.tdg.vroom.data.repository.local.CalendarMeetingRepository
import com.tdg.vroom.ext.DateTimeFormatExt
import com.tdg.vroom.ext.filterRangeDateAfter7Date
import com.tdg.vroom.ext.getDateFromTimeStamp
import com.tdg.vroom.ext.getTextTwoCharacter
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val calendarMeetingRepository: CalendarMeetingRepository
) : BaseViewModel() {

    private val _listCalendar = MutableLiveData<ArrayList<RecentModel>>()
    val listCalendar: LiveData<ArrayList<RecentModel>> = _listCalendar

    @SuppressLint("SimpleDateFormat")
    fun requestCalendar() {
        showLoading()
        CoroutineScope(Dispatchers.Main).launch {
            val listRecent = ArrayList<RecentModel>()
            hideLoading()

            val response = calendarMeetingRepository.getListCalendarMeeting()
            _listCalendar.postValue(arrayListOf())

            response.forEachIndexed { index, recentMeetingEntity ->
                val _listItem = recentMeetingEntity.itemsList?.split(",")
                val _listItemId = recentMeetingEntity.itemsListId?.split(",")
                val _listItemIsCalendar = recentMeetingEntity.itemsListCalendar?.split(",")
                val _listItemContactTime = recentMeetingEntity.itemsListContactTime?.split(",")
                val _listDataMeet = ArrayList<RecentModel.SubRecentModel>()

                _listItem?.forEachIndexed { index, eventName ->
                    val stateAddMeeting = _listItemIsCalendar?.get(index) == "1"
                    _listDataMeet.add(
                        RecentModel.SubRecentModel(
                            eventId = _listItemId?.get(index) ?: "",
                            txtImage = eventName.getTextTwoCharacter(),
                            roomName = eventName,
                            description = recentMeetingEntity.eventLocation ?: "",
                            time = "${recentMeetingEntity.dtStart} ${_listItemContactTime?.get(index)}",
                            backgroundColor = if (isDateCurrent(recentMeetingEntity.dtStart)) R.drawable.shape_circle_red else R.drawable.shape_circle_gray,
                            stateViewAddMeeting = stateAddMeeting
                        )
                    )
                }
                val textTitle = getTitleDateCalendar(recentMeetingEntity.dtStart)
                listRecent.add(
                    RecentModel(
                        title = textTitle ?: "",
                        date = recentMeetingEntity.dtStart ?: "",
                        dataMeet = _listDataMeet
                    )
                )
            }

            swapIndexListFindToday(listRecent)
            distanceListTitle(listRecent)
            val _listRangeDateAfter7Date = listRecent.filterRangeDateAfter7Date()

            _listCalendar.postValue(_listRangeDateAfter7Date as ArrayList<RecentModel>?)
        }
    }



    private fun distanceListTitle(listRecent: ArrayList<RecentModel>) {
        listRecent.forEachIndexed { index, recentModel ->
            val listDate = recentModel.dataMeet.distinctBy { it.roomName }
            listRecent[index].dataMeet = listDate as ArrayList<RecentModel.SubRecentModel>
        }
    }

    private fun swapIndexListFindToday(listRecent: ArrayList<RecentModel>) {
        if (listRecent.size > 1) {
            val findIndexToday = listRecent.indexOfLast {
                it.title == context.resources.getString(
                    R.string.title_text_today
                )
            }
            if (findIndexToday >= 0) {
                val listToday = listRecent[findIndexToday]
                listRecent[findIndexToday] = listRecent[0]
                listRecent[0] = listToday
            }
        }
    }

    private fun getTitleDateCalendar(dtStart: String?): String? {
        return if (isDateCurrent(dtStart)) {
            context.resources.getString(
                R.string.title_text_today
            )
        } else {
            dtStart
        }
    }

    private fun isDateCurrent(eventDate: String?): Boolean {
        return eventDate == getDateFromTimeStamp(outputFormat = DateTimeFormatExt.FORMAT_DEFAULT_DATE)
    }

    fun saveCalendar(param: CalendarEntity) {
        showLoading()
        var isUpdateData = false

        CoroutineScope(Dispatchers.Main).launch {
            val response = calendarMeetingRepository.getListCalendarMeeting()
            response.forEachIndexed { index, recentMeetingEntity ->
                val addItem = recentMeetingEntity.itemsList?.split(",")
                for (i in addItem?.indices!!) {
                    if (param.title == addItem[i]) {
                        isUpdateData = true
                        break
                    }
                }
            }

            if (isUpdateData) {
                val splitDateTime = param.dtStart?.split(" ")?.apply {
                    if (isNotEmpty()) this[0] else param.dtStart
                }
                calendarMeetingRepository.updateRecentMeeting(
                    eventId = param.eventId ?: "",
                    title = param.title ?: "",
                    dtStart = splitDateTime?.get(0).toString(),
                    isCalendar = param.isCalendar ?: false
                )
                isUpdateData = false
            } else {
                calendarMeetingRepository.saveCalendarMeeting(
                    calendarEntity = param
                )
            }
            hideLoading()
            requestCalendar()
        }
    }

    fun deleteCalendar() {
        CoroutineScope(Dispatchers.Main).launch {
            calendarMeetingRepository.deleteAll()
        }
    }

    fun getListAllCalendarMeeting(dateMeeting: String, callBack: (List<CalendarEntity>) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            val isInsertData = calendarMeetingRepository.getListAllCalendarMeeting().filter {
                it.dtStart == dateMeeting
            }
            callBack.invoke(isInsertData)
        }
    }

    fun saveCalendarFromEvent(param: CalendarEntity) {
        showLoading()
        CoroutineScope(Dispatchers.Main).launch {
            calendarMeetingRepository.saveCalendarMeeting(
                calendarEntity = param
            )
            hideLoading()
            requestCalendar()
        }
    }
}