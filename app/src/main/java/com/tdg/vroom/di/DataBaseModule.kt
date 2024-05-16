package com.tdg.vroom.di

import android.app.Application
import androidx.room.Room
import com.tdg.vroom.data.local.ConstantsRoom
import com.tdg.vroom.data.local.room.VRoomDatabase
import com.tdg.vroom.data.local.room.dao.CalendarMeetingDAO
import com.tdg.vroom.data.local.room.dao.RecentMeetingDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {
    @Singleton
    @Provides
    fun provideVroomDatabase(app: Application): VRoomDatabase {
        return Room.databaseBuilder(
            app,
            VRoomDatabase::class.java,
            ConstantsRoom.VROOM_DB_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideRecentMeetingDao(vroomDatabase: VRoomDatabase): RecentMeetingDAO {
        return vroomDatabase.getRecentMeetingDAO()
    }

    @Singleton
    @Provides
    fun provideCalendarMeetingDao(vroomDatabase: VRoomDatabase): CalendarMeetingDAO {
        return vroomDatabase.getCalendarMeetingDAO()
    }
}
