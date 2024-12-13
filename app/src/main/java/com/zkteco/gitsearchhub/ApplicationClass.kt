package com.zkteco.gitsearchhub


import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ApplicationClass: Application() {
    companion object {
        var instance: ApplicationClass? = null
    }

    override fun onCreate() {
        super.onCreate()
        instance = this


    }
}
