package com.tdg.vroom.data.repository.local

import com.tdg.vroom.data.local.room.dao.CalendarMeetingDAO
import com.tdg.vroom.data.local.room.entity.CalendarEntity
import javax.inject.Inject

interface CalendarMeetingRepository {
    suspend fun saveCalendarMeeting(calendarEntity: CalendarEntity)
    suspend fun getListCalendarMeeting(): List<CalendarEntity>
    suspend fun updateRecentMeeting(eventId: String, title: String, dtStart: String, isCalendar:Boolean)
    suspend fun deleteAll()
    suspend fun getListAllCalendarMeeting(): List<CalendarEntity>
}

class CalendarMeetingRepositoryImpl @Inject constructor(
    private val calendarMeetingDAO: CalendarMeetingDAO
) : CalendarMeetingRepository {
    override suspend fun saveCalendarMeeting(calendarEntity: CalendarEntity) {
        return calendarMeetingDAO.saveCalendarMeeting(calendarEntity = calendarEntity)
    }

    override suspend fun getListCalendarMeeting(): List<CalendarEntity> {
        return calendarMeetingDAO.getListCalendarMeeting()
    }

    override suspend fun updateRecentMeeting(eventId: String, title: String, dtStart: String, isCalendar:Boolean) {
        return calendarMeetingDAO.updateRecentMeeting(
            eventId = eventId,
            title = title,
            dtStart = dtStart,
            isCalendar = isCalendar
        )
    }

    override suspend fun deleteAll() {
        return calendarMeetingDAO.deleteAll()
    }

    override suspend fun getListAllCalendarMeeting(): List<CalendarEntity> {
        return calendarMeetingDAO.isCheckInsertCalendarMeeting()
    }
}
