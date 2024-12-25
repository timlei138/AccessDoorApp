package com.acs.mqttclient

import android.content.Context
import android.util.Log
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.util.concurrent.atomic.AtomicBoolean

class MqttClientManager() {

    private val LogTag = "MqttClientManager"

    private lateinit var mqttAndroidClient: MqttAndroidClient
    private var connected = AtomicBoolean(false)

    fun initMqttClient(context: Context,server: String,clientId: String){
        mqttAndroidClient = MqttAndroidClient(context,server,clientId)
        mqttAndroidClient.setCallback(mqttCallback)
    }


    fun connect(user: String,pwd: String){
        if (isConnect()) return
        val options = MqttConnectOptions().apply {
            userName = user
            password = pwd.toCharArray()
        }
        try {
            mqttAndroidClient.connect(options,object : IMqttActionListener{
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    connected.set(true)
                    Log.w(LogTag,"connect onSuccess")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    connected.set(false)
                    Log.w(LogTag,"connect onFailure")
                    exception?.printStackTrace()
                }
            })
        }catch (e: MqttException){
            Log.w(LogTag,"connect failed ${e.message}")
            e.printStackTrace()
        }
    }



    fun subscribe(topic: String,qos: Int = 1){
        if (isConnect()){
            try {
                mqttAndroidClient.subscribe(topic,qos,null,object : IMqttActionListener{
                    override fun onSuccess(asyncActionToken: IMqttToken?) {
                        Log.w(LogTag,"subscribe topic[$topic] onSuccess")
                    }

                    override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                        Log.w(LogTag,"subscribe topic[$topic] onFailure ${exception?.message}")
                        exception?.printStackTrace()
                    }

                })
            }catch (e: MqttException){
                e.printStackTrace()
            }
        }else
            Log.w(LogTag,"Connected is Loss")


    }

    fun unSubscribe(topic: String){
        if (isConnect()){
            try {
                mqttAndroidClient.unsubscribe(topic,null,object : IMqttActionListener{
                    override fun onSuccess(asyncActionToken: IMqttToken?) {
                        Log.w(LogTag,"unSubscribe topic[$topic] onSuccess")
                    }

                    override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                        Log.w(LogTag,"unSubscribe topic[$topic] onFailure ${exception?.message}")
                        exception?.printStackTrace()
                    }

                })
            }catch (e: MqttException){
                e.printStackTrace()
            }
        }else
            Log.w(LogTag,"Connected is Loss")

    }

    fun publish(topic: String,msg: String, qos: Int = 1,retained: Boolean = false){
        if (isConnect()){
            val msg = MqttMessage().apply {
                this.payload = msg.toByteArray()
                this.qos = qos
                isRetained = retained
            }
            try {
                mqttAndroidClient.publish(topic,msg,null,object : IMqttActionListener{
                    override fun onSuccess(asyncActionToken: IMqttToken?) {
                        Log.w(LogTag,"publish topic[$topic] onSuccess")
                    }

                    override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                        Log.w(LogTag,"publish topic[$topic] onFailure ${exception?.message}")
                        exception?.printStackTrace()
                    }

                })
            }catch (e: MqttException){
                e.printStackTrace()
            }
        }else
            Log.w(LogTag,"Connected is Loss")

    }



    fun disconnect(){
        if (isConnect()){
            try {
                mqttAndroidClient.disconnect(null,object : IMqttActionListener{
                    override fun onSuccess(asyncActionToken: IMqttToken?) {
                        Log.w(LogTag,"disconnect onSuccess")
                    }

                    override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                        Log.w(LogTag,"disconnect onFailure ${exception?.message}")
                        exception?.printStackTrace()
                    }

                })
            }catch (e: MqttException){
                e.printStackTrace()
            }
        }else
            Log.w(LogTag,"Connected is Loss")

    }

    fun isConnect(): Boolean = mqttAndroidClient.isConnected && connected.get()


    private val mqttCallback = object : MqttCallback{
        override fun connectionLost(cause: Throwable?) {

        }

        override fun messageArrived(topic: String?, message: MqttMessage?) {

        }

        override fun deliveryComplete(token: IMqttDeliveryToken?) {

        }

    }
}

val mqttClient by lazy {
    MqttClientManager()
}