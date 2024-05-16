package com.tdg.vroom.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.google.gson.GsonBuilder
import com.tdg.vroom.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory

open class NetworkHttpOptions {

    fun provideOkHttpBuilder(context: Context): OkHttpClient.Builder {

        val okHttpBuilder = OkHttpClient.Builder()
        val logging = HttpLoggingInterceptor()

        if (BuildConfig.DEBUG) {
            logging.level = (HttpLoggingInterceptor.Level.BODY)
        }

        okHttpBuilder.apply {
            addInterceptor {
                val original: Request = it.request()
                val request: Request = original.newBuilder()
                    .header("Content-Type", "application/xml")
                    .header("Accept", "application/xml")
                    .build()
                it.proceed(request)
            }
            addInterceptor(logging)
            //addInterceptor(provideChuckerInterceptor(context))
        }
        return okHttpBuilder
    }

    private fun provideChuckerInterceptor(context: Context): ChuckerInterceptor {
        val collector = ChuckerCollector(
            context = context,
            showNotification = true,
            retentionPeriod = RetentionManager.Period.ONE_HOUR
        )

        return ChuckerInterceptor.Builder(context)
            .collector(collector)
            .maxContentLength(250000L)
            .redactHeaders(emptySet())
            .alwaysReadResponseBody(true)
            .build()
    }

    fun gsonConverterFactory(): GsonConverterFactory {
        val gsonBuilder = GsonBuilder().setLenient()
        val gson = gsonBuilder.serializeNulls().create()
        return GsonConverterFactory.create(gson)
    }
}