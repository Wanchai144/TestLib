package com.tdg.vroom.di

import com.tdg.vroom.data.repository.GeneralRepository
import com.tdg.vroom.data.repository.GeneralRepositoryImpl
import com.tdg.vroom.data.repository.local.CalendarMeetingRepository
import com.tdg.vroom.data.repository.local.CalendarMeetingRepositoryImpl
import com.tdg.vroom.data.repository.local.RecentMeetingRepository
import com.tdg.vroom.data.repository.local.RecentMeetingRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@InstallIn(ViewModelComponent::class)
@Module
abstract class RepositoryBindsModule {

    @Binds
    @ViewModelScoped
    abstract fun bindsGeneralRepository(
        generalRepositoryImpl: GeneralRepositoryImpl
    ): GeneralRepository

    @Binds
    @ViewModelScoped
    abstract fun bindsLocalRecentMeetingRepository(
        recentMeetingRepositoryImpl: RecentMeetingRepositoryImpl
    ): RecentMeetingRepository

    @Binds
    @ViewModelScoped
    abstract fun bindsLocalCalendarMeetingRepository(
        calendarMeetingRepositoryImpl: CalendarMeetingRepositoryImpl
    ): CalendarMeetingRepository

}