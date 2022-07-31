package tech.siham.papp

import android.app.Application

class application: Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: application
            private set
    }
}
