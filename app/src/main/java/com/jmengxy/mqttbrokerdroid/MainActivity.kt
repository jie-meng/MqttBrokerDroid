package com.jmengxy.mqttbrokerdroid

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jmengxy.mqttbroker.MqttBroker
import java.util.*

class MainActivity : AppCompatActivity() {

    private val mqttBroker = MqttBroker()

    private val text by lazy { findViewById<TextView>(R.id.text) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            val properties = Properties()
            properties.setProperty("tcp_port", "1884")
            mqttBroker.start(properties)
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
