package com.tdg.vroom.di

import android.content.Context
import com.tdg.vroom.data.local.preferences.PreferenceUtility
import com.tdg.vroom.data.local.preferences.SharePreferenceUtility
import com.tdg.vroom.utils.CalendarEvent
import com.tdg.vroom.utils.dialog.DialogUtils
import com.tdg.vroom.utils.locale.LocaleHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UtilsModule {

    @Provides
    @Singleton
    fun provideSharedPreference(sharePreferenceUtility: PreferenceUtility): SharePreferenceUtility =
        sharePreferenceUtility

    @Provides
    @Singleton
    fun provideLocale(
        @ApplicationContext context: Context,
        sharePreferenceUtility: PreferenceUtility
    ): LocaleHelper =
        LocaleHelper(context, sharePreferenceUtility)

    @Provides
    @Singleton
    fun provideDialogUtils(
        @ApplicationContext context: Context,
    ): DialogUtils = DialogUtils(context)

    @Provides
    @Singleton
    fun provideCalendarEvent(
        @ApplicationContext context: Context,
    ): CalendarEvent = CalendarEvent(context)

}