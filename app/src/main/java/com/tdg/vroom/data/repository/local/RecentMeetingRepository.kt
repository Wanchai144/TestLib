package com.tdg.vroom.data.repository.local

import com.tdg.vroom.data.local.room.dao.RecentMeetingDAO
import com.tdg.vroom.data.local.room.entity.RecentMeetingEntity
import javax.inject.Inject

interface RecentMeetingRepository {
    suspend  fun saveRecentMeeting(recentMeetingEntity: RecentMeetingEntity)
    suspend fun getListRecentMeeting(): List<RecentMeetingEntity>
    suspend fun updateRecentMeeting(eventName: String, eventDate: String, eventDateTime: String)
    suspend fun doDeleteAll()
}

class RecentMeetingRepositoryImpl @Inject constructor(
    private val recentMeetingDAO: RecentMeetingDAO
) : RecentMeetingRepository {

    override suspend fun saveRecentMeeting(recentMeetingEntity: RecentMeetingEntity){
        return recentMeetingDAO.saveRecentMeeting(recentMeetingEntity = recentMeetingEntity)
    }

    override suspend fun getListRecentMeeting(): List<RecentMeetingEntity> {
       return recentMeetingDAO.getListRecentMeeting()
    }

    override suspend fun updateRecentMeeting(
        eventName: String,
        eventDate: String,
        eventDateTime: String
    ) {
        return recentMeetingDAO.updateRecentMeeting(
            eventName = eventName,
            eventDate = eventDate,
            eventDateTime = eventDateTime
        )
    }

    override suspend fun doDeleteAll() {
        return recentMeetingDAO.deleteAll()
    }
}
