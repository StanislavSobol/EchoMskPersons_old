package com.gmail.echomskfan.persons

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
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

        fun getAppContext(): Context = instance.applicationContext

        fun isOnlineWithToast(showToastIfNot: Boolean): Boolean {
            val cm = instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo

            val result = netInfo != null && netInfo.isConnectedOrConnecting

            if (showToastIfNot && !result) {
                val s = instance.resources.getString(R.string.error_no_internet)
                Toast.makeText(instance, s, Toast.LENGTH_LONG).show()
            }

            return result
        }
    }
}
