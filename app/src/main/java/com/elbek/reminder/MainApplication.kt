package com.elbek.reminder

import android.app.Application
import android.content.Context

class MainApplication : Application() {

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()

        //initDI(applicationContext)
    }

    companion object {

        private var instance: MainApplication? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }
}