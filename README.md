# MqttBrokerDroid

MqttBrokerDroid is [JMqtt](https://github.com/Cicizz/jmqtt) on Android.

## How to import MqttBrokerDroid library into Android project

1. Add the JitPack repository to your build file
    - gradle

        Add it in your root build.gradle at the end of repositories:

        ```
        allprojects {
            repositories {
                ...
                maven { url 'https://jitpack.io' }
            }
        }
        ```

    - maven

        ```
        <repositories>
            <repository>
                <id>jitpack.io</id>
                <url>https://jitpack.io</url>
            </repository>
        </repositories>
        ```

2. Add the dependency
    - gradle

        ```
        dependencies {
                implementation 'com.github.jie-meng:MqttBrokerDroid:1.1.0'
        }
        ```

    - maven

        ```
        <dependency>
            <groupId>com.github.jie-meng</groupId>
            <artifactId>MqttBrokerDroid</artifactId>
            <version>1.1.0</version>
        </dependency>
        ```

## How to start broker

```
package com.jmengxy.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jmengxy.mqttbroker.MqttBroker

class MainActivity : AppCompatActivity() {

    private val mqttBroker = MqttBroker()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mqttBroker.start()
    }

    override fun onDestroy() {
        mqttBroker.stop()
        super.onDestroy()
    }
}

```
