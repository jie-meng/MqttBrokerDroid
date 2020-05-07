package com.jmengxy.mqttbroker

import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.joran.JoranConfigurator
import org.apache.commons.cli.CommandLineParser
import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.Option
import org.apache.commons.cli.Options
import org.apache.commons.lang3.StringUtils
import org.jmqtt.broker.BrokerController
import org.jmqtt.common.config.BrokerConfig
import org.jmqtt.common.config.ClusterConfig
import org.jmqtt.common.config.NettyConfig
import org.jmqtt.common.config.StoreConfig
import org.jmqtt.common.helper.MixAll
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.IOException
import java.util.*

class MqttBroker {
    private var brokerController: BrokerController? = null

    @Throws(Exception::class)
    fun start(args: Array<String> = emptyArray()) {
        stop()

        val options = buildOptions()
        val parser: CommandLineParser = DefaultParser()
        val commandLine = parser.parse(options, args)
        var jmqttHome: String? = null
        var jmqttConfigPath: String? = null
        val brokerConfig = BrokerConfig()
        val nettyConfig = NettyConfig()
        val storeConfig = StoreConfig()
        val clusterConfig = ClusterConfig()
        if (commandLine != null) {
            jmqttHome = commandLine.getOptionValue("h")
            jmqttConfigPath = commandLine.getOptionValue("c")
        }
        if (StringUtils.isNotEmpty(jmqttConfigPath)) {
            initConfig(jmqttConfigPath, brokerConfig, nettyConfig, storeConfig, clusterConfig)
        }
        if (StringUtils.isEmpty(jmqttHome)) {
            jmqttHome = brokerConfig.jmqttHome
        }
        //        if (StringUtils.isEmpty(jmqttHome)) {
//            throw new Exception("please set JMQTT_HOME.");
//        }
        val lc =
            LoggerFactory.getILoggerFactory() as LoggerContext
        val configurator = JoranConfigurator()
        configurator.context = lc
        lc.reset()
        //        configurator.doConfigure(jmqttHome + "/conf/logback_broker.xml");
        brokerController =
            BrokerController(brokerConfig, nettyConfig, storeConfig, clusterConfig)
        brokerController?.start()
    }

    fun stop() {
        brokerController?.let {
            it?.shutdown()
        }
        brokerController = null
    }

    private fun buildOptions(): Options {
        val options = Options()
        var opt =
            Option("h", true, "jmqttHome,eg: /wls/xxx")
        opt.isRequired = false
        options.addOption(opt)
        opt = Option(
            "c",
            true,
            "jmqtt.properties path,eg: /wls/xxx/xxx.properties"
        )
        opt.isRequired = false
        options.addOption(opt)
        return options
    }

    private fun initConfig(
        jmqttConfigPath: String?,
        brokerConfig: BrokerConfig,
        nettyConfig: NettyConfig,
        storeConfig: StoreConfig,
        clusterConfig: ClusterConfig
    ) {
        val properties = Properties()
        var bufferedReader: BufferedReader? = null
        try {
            bufferedReader = BufferedReader(FileReader(jmqttConfigPath))
            properties.load(bufferedReader)
            MixAll.properties2POJO(properties, brokerConfig)
            MixAll.properties2POJO(properties, nettyConfig)
            MixAll.properties2POJO(properties, storeConfig)
            MixAll.properties2POJO(properties, clusterConfig)
        } catch (e: FileNotFoundException) {
            println("jmqtt.properties cannot find,cause = $e")
        } catch (e: IOException) {
            println("Handle jmqttConfig IO exception,cause = $e")
        } finally {
            try {
                if (Objects.nonNull(bufferedReader)) {
                    bufferedReader!!.close()
                }
            } catch (e: IOException) {
                println("Handle jmqttConfig IO exception,cause = $e")
            }
        }
    }
}