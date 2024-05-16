package com.tdg.vroom.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.google.gson.GsonBuilder
import com.tdg.vroom.BuildConfig
import com.tdg.vroom.data.remote.api.GeneralApiService
import com.tdg.vroom.data.remote.api.LiveKitApiService
import com.tdg.vroom.data.remote.api.PackageApiService
import com.tdg.vroom.data.remote.api.VersionMeetingApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

const val retrofitLiveKit = "retrofitLiveKit"
const val retrofitGeneral = "retrofitGeneral"
const val retrofitGetPackage = "retrofitGetPackage"
const val retrofitVersionMeeting = "retrofitVersionMeeting"

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule : NetworkHttpOptions() {

    @Singleton
    @Provides
    @Named(retrofitLiveKit)
    fun provideLiveKitRetrofit(@ApplicationContext context: Context): Retrofit {

        return Retrofit.Builder()
            .baseUrl(BuildConfig.LIVEKIT_API_URL)
            .addConverterFactory(gsonConverterFactory())
            .client(provideOkHttpBuilder(context = context).build())
            .build()
    }

    @Singleton
    @Provides
    @Named(retrofitGeneral)
    fun provideGeneralRetrofit(@ApplicationContext context: Context): Retrofit {

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(gsonConverterFactory())
            .client(provideOkHttpBuilder(context = context).build())
            .build()
    }

    @Singleton
    @Provides
    @Named(retrofitGetPackage)
    fun provideGetPackageRetrofit(@ApplicationContext context: Context): Retrofit {

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL_PACKAGE)
            .addConverterFactory(gsonConverterFactory())
            .client(provideOkHttpBuilder(context = context).build())
            .build()
    }

    @Singleton
    @Provides
    @Named(retrofitVersionMeeting)
    fun provideVersionMeetingRetrofit(@ApplicationContext context: Context): Retrofit {

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL_VERION_MEETING)
            .addConverterFactory(gsonConverterFactory())
            .client(provideOkHttpBuilder(context = context).build())
            .build()
    }

    @Singleton
    @Provides
    fun provideLiveKitService(@Named(retrofitLiveKit) retrofit: Retrofit): LiveKitApiService {
        return retrofit.create(LiveKitApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideGeneralService(@Named(retrofitGeneral) retrofit: Retrofit): GeneralApiService {
        return retrofit.create(GeneralApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideGetPackageService(@Named(retrofitGetPackage) retrofit: Retrofit): PackageApiService {
        return retrofit.create(PackageApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideVersionMeetingService(@Named(retrofitVersionMeeting) retrofit: Retrofit): VersionMeetingApiService {
        return retrofit.create(VersionMeetingApiService::class.java)
    }
}