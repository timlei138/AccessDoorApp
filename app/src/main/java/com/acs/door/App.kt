package com.acs.door

import android.app.Application
import android.content.Context
import com.acs.mqttclient.MqttClientManager
import kotlin.properties.Delegates

class App : Application(){

    var appCtx by Delegates.notNull<Context>()
        private set

    override fun onCreate() {
        super.onCreate()
        appCtx = this
    }
}