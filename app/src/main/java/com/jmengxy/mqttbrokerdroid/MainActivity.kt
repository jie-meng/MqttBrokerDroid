package com.jmengxy.mqttbrokerdroid

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jmengxy.mqttbroker.MqttBroker

class MainActivity : AppCompatActivity() {

    private val mqttBroker = MqttBroker()

    private val text by lazy { findViewById<TextView>(R.id.text) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            mqttBroker.start()
            text.text = "MqttBroker started"
        } catch (e: Exception) {
            e.printStackTrace()
            text.text = e.message
        }
    }

    override fun onDestroy() {
        mqttBroker.stop()
        super.onDestroy()
    }
}
