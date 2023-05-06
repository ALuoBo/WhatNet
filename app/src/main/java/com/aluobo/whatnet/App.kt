package com.aluobo.whatnet

import android.app.Application
import android.content.Context
import kotlin.properties.Delegates

class App: Application() {

    override fun onCreate() {
        appContext = this
        super.onCreate()
    }

    companion object {
        var appContext by Delegates.notNull<Context>()
    }
}