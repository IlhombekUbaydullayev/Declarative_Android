package com.example.android_delerative

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DemosApplication:Application() {

    override fun onCreate() {
        super.onCreate()
    }
}