package com.shiryaev.schedule.util

import android.app.Application
import com.shiryaev.schedule.interfaces.AppComponent
import com.shiryaev.schedule.interfaces.DaggerAppComponent

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        component = DaggerAppComponent.create()
    }

    companion object {
        private var component: AppComponent? = null
        private lateinit var instance: App

        fun getComponent() : AppComponent? {
            return component
        }

        fun getInstance() : App {
            return instance
        }
    }
}