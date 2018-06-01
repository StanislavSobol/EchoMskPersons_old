package com.gmail.echomskfan.persons

import android.app.Application
import android.content.Context

class MApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: MApplication
        fun getAppContext():Context = instance.applicationContext
    }
}
