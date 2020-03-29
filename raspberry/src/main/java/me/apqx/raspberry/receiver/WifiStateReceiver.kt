package me.apqx.raspberry.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import me.apqx.jetlog.LogUtil
import me.apqx.raspberry.utils.WifiUtils

class WifiStateReceiver : BroadcastReceiver() {
    private lateinit var wifiManager: WifiManager;

    override fun onReceive(context: Context?, intent: Intent) {
        if (intent.action == WifiManager.SCAN_RESULTS_AVAILABLE_ACTION) {
            LogUtil.i("SCAN_RESULTS_AVAILABLE_ACTION onReceive")
            if (!this::wifiManager.isInitialized)
                wifiManager = context?.getSystemService(Context.WIFI_SERVICE) as WifiManager
            // 获取扫描到的wifi
            LogUtil.d("scannedWifi : ${WifiUtils.generateScannedWifiStringList(wifiManager)}")
            // 获取已经配置过的wifi
            LogUtil.d("configuredWifi ${WifiUtils.generateConfiguredWifiStringList(wifiManager)}")
            // 获取当前连接的wifi
            LogUtil.d("connectedWifi ${WifiUtils.generateCurrentWifiString(wifiManager)}")
            LogUtil.d("currentIp ${WifiUtils.getCurrentIp(wifiManager)}")
        } else if (intent.action == WifiManager.NETWORK_STATE_CHANGED_ACTION) {
            LogUtil.i("NETWORK_STATE_CHANGED_ACTION onReceive")
            if (!this::wifiManager.isInitialized)
                wifiManager = context?.getSystemService(Context.WIFI_SERVICE) as WifiManager
            // 获取扫描到的wifi
            LogUtil.d("scannedWifi : ${WifiUtils.generateScannedWifiStringList(wifiManager)}")
            // 获取已经配置过的wifi
            LogUtil.d("configuredWifi ${WifiUtils.generateConfiguredWifiStringList(wifiManager)}")
            // 获取当前连接的wifi
            LogUtil.d("connectedWifi ${WifiUtils.generateCurrentWifiString(wifiManager)}")
            LogUtil.d("currentIp ${WifiUtils.getCurrentIp(wifiManager)}")
        }
    }
}