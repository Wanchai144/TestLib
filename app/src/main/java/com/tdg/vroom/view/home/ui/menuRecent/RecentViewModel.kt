package com.tdg.vroom.view.home.ui.menuRecent

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tdg.vroom.R
import com.tdg.vroom.base.BaseViewModel
import com.tdg.vroom.data.local.room.entity.RecentMeetingEntity
import com.tdg.vroom.data.model.RecentModel
import com.tdg.vroom.data.repository.local.RecentMeetingRepository
import com.tdg.vroom.ext.DateTimeFormatExt.Companion.FORMAT_DATE_TIME_SECOND
import com.tdg.vroom.ext.DateTimeFormatExt.Companion.FORMAT_DEFAULT_DATE
import com.tdg.vroom.ext.getDateFromTimeStamp
import com.tdg.vroom.ext.getTextTwoCharacter
import com.tdg.vroom.ext.getYesterdayDateString
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecentViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val recentMeetingRepository: RecentMeetingRepository
) : BaseViewModel() {

    private val _listRecent = MutableLiveData<ArrayList<RecentModel>>()
    val listRecent: LiveData<ArrayList<RecentModel>> = _listRecent

    fun requestHistory() {
        showLoading()
        CoroutineScope(Dispatchers.Main).launch {
            val listRecent = ArrayList<RecentModel>()
            hideLoading()
            val response = recentMeetingRepository.getListRecentMeeting()

            response.forEachIndexed { index, recentMeetingEntity ->
                val addItem = recentMeetingEntity.itemsList?.split(",")
                val _listDataMeet = ArrayList<RecentModel.SubRecentModel>()
                addItem?.forEachIndexed { index, eventName ->
                    _listDataMeet.add(
                        RecentModel.SubRecentModel(
                            eventId = recentMeetingEntity.id.toString(),
                            txtImage = eventName.getTextTwoCharacter(),
                            roomName = eventName,
                            description = eventName,
                            time = recentMeetingEntity.eventDate ?: "",
                            backgroundColor =
                            if (isDateCurrent(recentMeetingEntity.eventDate)) R.drawable.shape_circle_red else R.drawable.shape_circle_gray
                        )
                    )
                }
                listRecent.add(
                    RecentModel(
                        title = if (isDateCurrent(recentMeetingEntity.eventDate))
                            context.resources.getString(
                                R.string.title_text_today
                            ) else context.getYesterdayDateString(
                            recentMeetingEntity.eventDate ?: ""
                        ),
                        date = recentMeetingEntity.eventDate ?: "",
                        dataMeet = _listDataMeet
                    )
                )
            }
            _listRecent.postValue(listRecent)
        }
    }

    private fun isDateCurrent(eventDate: String?): Boolean {
        return eventDate == getDateFromTimeStamp(outputFormat = FORMAT_DEFAULT_DATE)
    }

    fun saveHistory(eventName: String) {
        showLoading()
        var isUpdateData = false
        CoroutineScope(Dispatchers.Main).launch {
            val response = recentMeetingRepository.getListRecentMeeting()
            response.forEachIndexed { index, recentMeetingEntity ->
                val addItem = recentMeetingEntity.itemsList?.split(",")
                for (i in addItem?.indices!!) {
                    if (eventName == addItem[i]) {
                        isUpdateData = true
                        break
                    }
                }
            }
            if (isUpdateData) {
                recentMeetingRepository.updateRecentMeeting(
                    eventName = eventName,
                    eventDate = getDateFromTimeStamp(outputFormat = FORMAT_DEFAULT_DATE),
                    eventDateTime = getDateFromTimeStamp(outputFormat = FORMAT_DATE_TIME_SECOND),
                )
            } else {
                recentMeetingRepository.saveRecentMeeting(
                    recentMeetingEntity = RecentMeetingEntity(
                        id = null,
                        eventName = eventName,
                        eventDate = getDateFromTimeStamp(outputFormat = FORMAT_DEFAULT_DATE),
                        eventDateTime = getDateFromTimeStamp(outputFormat = FORMAT_DATE_TIME_SECOND),
                    )
                )
            }
            hideLoading()
            requestHistory()
        }
    }
}