package com.tdg.vroom.data.local.room.dao

import androidx.room.*
import com.tdg.vroom.data.local.ConstantsRoom.TB_RECENT_MEETING
import com.tdg.vroom.data.local.room.entity.RecentMeetingEntity

@Dao
interface RecentMeetingDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRecentMeeting(recentMeetingEntity: RecentMeetingEntity)

    @Query("SELECT eventDate,GROUP_CONCAT(eventName) as itemsList FROM $TB_RECENT_MEETING GROUP BY eventDate ORDER BY MIN(eventDate) desc")
    suspend fun getListRecentMeeting(): List<RecentMeetingEntity>

    @Query("UPDATE $TB_RECENT_MEETING SET eventDate=:eventDate, eventDateTime=:eventDateTime WHERE eventName = :eventName")
    suspend fun updateRecentMeeting(eventName: String, eventDate: String, eventDateTime: String)

    @Query("DELETE FROM $TB_RECENT_MEETING")
    suspend fun deleteAll()

}
