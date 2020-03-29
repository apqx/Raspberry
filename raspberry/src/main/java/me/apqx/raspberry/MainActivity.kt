package me.apqx.raspberry

import android.content.Context
import android.content.IntentFilter
import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import me.apqx.jetlog.ILogUIListener
import me.apqx.jetlog.LogUtil
import me.apqx.raspberry.receiver.WifiStateReceiver
import me.apqx.raspberry.utils.WifiUtils

/**
 * Skeleton of an Android Things activity.
 *
 * Android Things peripheral APIs are accessible through the PeripheralManager
 * For example, the snippet below will open a GPIO pin and set it to HIGH:
 *
 * val manager = PeripheralManager.getInstance()
 * val gpio = manager.openGpio("BCM6").apply {
 *     setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
 * }
 * gpio.value = true
 *
 * You can find additional examples on GitHub: https://github.com/androidthings
 */
class MainActivity : AppCompatActivity(), ILogUIListener {
    private lateinit var wifiManager: WifiManager
    private lateinit var wifiStateReceiver: WifiStateReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        LogUtil.registerUIListener(this)
        wifiManager = getSystemService(Context.WIFI_SERVICE) as WifiManager

        tgb_scan_wifi.setOnCheckedChangeListener { btn, checked ->
            if (checked) {
                LogUtil.d("start scan wifi")
                // 开始扫描wifi
                wifiManager.startScan()
                // 注册扫描结果监听器
                registerWifiStateReceiver()
            } else {
                LogUtil.d("stop scan wifi")
                unregisterWifiStateReceiver()
            }
        }
        btn_connect_apqx.setOnClickListener {
            WifiUtils.connectToWifi(wifiManager, "APQX", "apq133536")

        }
        btn_connect_ziru.setOnClickListener {
            // 连接到自如
            WifiUtils.connectToWifi(wifiManager, "ziroom_1403", "4001001111")

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtil.unregisterUIListener(this)
    }

    override fun log(log: String) {
        val hint = "${tv_log.text}\n$log"
        tv_log.text = hint
    }

    private fun unregisterWifiStateReceiver() {
        unregisterReceiver(wifiStateReceiver)
    }

    private fun registerWifiStateReceiver() {
        if (!this::wifiStateReceiver.isInitialized) {
            wifiStateReceiver = WifiStateReceiver()
        }
        val intentFilter = IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
        registerReceiver(wifiStateReceiver, intentFilter)
    }
}

