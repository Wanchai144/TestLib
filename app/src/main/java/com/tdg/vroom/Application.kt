package com.tdg.vroom

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.livekit.android.LiveKit
import io.livekit.android.util.LoggingLevel

@HiltAndroidApp
class Application : Application() {

    override fun onCreate() {
        super.onCreate()

        LiveKit.loggingLevel = LoggingLevel.OFF
    }
}