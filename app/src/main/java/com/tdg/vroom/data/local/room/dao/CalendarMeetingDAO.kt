package com.tdg.vroom.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tdg.vroom.data.local.ConstantsRoom.TB_CALENDAR_MEETING
import com.tdg.vroom.data.local.room.entity.CalendarEntity

@Dao
interface CalendarMeetingDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCalendarMeeting(calendarEntity: CalendarEntity)

    @Query("SELECT dtStart,GROUP_CONCAT(title) as itemsList,GROUP_CONCAT(eventId) as itemsListId,GROUP_CONCAT(dtStart) as itemsListDate,GROUP_CONCAT(isCalendar) as itemsListCalendar,GROUP_CONCAT(time) as itemsListContactTime,isCalendar,eventLocation,eventId FROM $TB_CALENDAR_MEETING GROUP BY dtStart ORDER BY MIN(dtStart) asc")
    suspend fun getListCalendarMeeting(): List<CalendarEntity>

    @Query("SELECT * FROM $TB_CALENDAR_MEETING")
    suspend fun isCheckInsertCalendarMeeting(): List<CalendarEntity>

    @Query("UPDATE $TB_CALENDAR_MEETING SET title=:title, dtStart=:dtStart,isCalendar=:isCalendar WHERE eventId = :eventId")
    suspend fun updateRecentMeeting(
        eventId: String,
        title: String,
        dtStart: String,
        isCalendar: Boolean
    )

    @Query("DELETE FROM $TB_CALENDAR_MEETING")
    suspend fun deleteAll()

}
