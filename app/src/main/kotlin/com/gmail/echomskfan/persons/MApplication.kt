package com.gmail.echomskfan.persons

import android.app.Application
import com.gmail.echomskfan.persons.di.DaggerComponents
import com.gmail.echomskfan.persons.di.DaggerDaggerComponents
import com.gmail.echomskfan.persons.di.DaggerModules

class MApplication : Application() {

    private lateinit var dagger2Components: DaggerComponents

    override fun onCreate() {
        super.onCreate()
        instance = this
        dagger2Components = DaggerDaggerComponents.builder().daggerModules(DaggerModules()).build()
    }

    companion object {
        lateinit var instance: MApplication

        fun getDaggerComponents(): DaggerComponents {
            return instance.dagger2Components
        }
    }
}
