package com.tdg.vroom.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tdg.vroom.data.local.ConstantsRoom
import com.tdg.vroom.data.local.room.dao.CalendarMeetingDAO
import com.tdg.vroom.data.local.room.entity.RecentMeetingEntity
import com.tdg.vroom.data.local.room.dao.RecentMeetingDAO
import com.tdg.vroom.data.local.room.entity.CalendarEntity

@Database(
    entities = [
        RecentMeetingEntity::class,
        CalendarEntity::class
    ],
    version = ConstantsRoom.VROOM_DB_VERSION,
    exportSchema = false
)

abstract class VRoomDatabase : RoomDatabase() {

    abstract fun getRecentMeetingDAO(): RecentMeetingDAO
    abstract fun getCalendarMeetingDAO(): CalendarMeetingDAO
}

