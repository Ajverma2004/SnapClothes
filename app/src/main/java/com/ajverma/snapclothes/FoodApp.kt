package com.ajverma.snapclothes

import android.app.Application
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FoodApp: Application() {
    override fun onCreate() {
        super.onCreate()
        AppEventsLogger.activateApp(this)
    }
}