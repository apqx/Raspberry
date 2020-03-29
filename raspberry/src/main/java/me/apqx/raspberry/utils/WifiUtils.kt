package me.apqx.raspberry.utils

import android.net.wifi.*
import android.text.TextUtils
import me.apqx.jetlog.LogUtil
import java.util.regex.Pattern

object WifiUtils {

    /**
     * 生成扫描到的wifi名列表
     */
    fun generateScannedWifiStringList(wifiManager: WifiManager): List<String> {
        val list = ArrayList<String>()
        for (item in wifiManager.scanResults) {
            if (!TextUtils.isEmpty(item.SSID)) {
                list.add(item.SSID)
            }
        }
        return list
    }

    /**
     * 生成已经配置过的wifi名列表
     */
    fun generateConfiguredWifiStringList(wifiManager: WifiManager): List<String> {
        val list = ArrayList<String>()
        for (item in wifiManager.configuredNetworks) {
            if (!TextUtils.isEmpty(item.getParseSSID())) {
                list.add(item.getParseSSID())
            }
        }
        return list
    }

    /**
     * 生成当前连接的wifi名
     */
    fun generateCurrentWifiString(wifiManager: WifiManager): String {
        return wifiManager.connectionInfo.getParseSSID()
    }

    /**
     * 获取当前的ip地址
     */
    fun getCurrentIp(wifiManager: WifiManager): String {
        return wifiManager.connectionInfo.getParseIp()
    }

    /**
     * 从扫描到的wifi列表中查找目标wifi
     */
    private fun findInScannedWifi(wifiManager: WifiManager, targetSSID: String): ScanResult? {
        for (scanResult in wifiManager.scanResults) {
            if (targetSSID == scanResult.SSID) {
                return scanResult
            }
        }
        return null
    }

    /**
     * 从配置过的wifi列表中查找目标wifi
     */
    private fun findInConfiguredWifi(wifiManager: WifiManager, targetSSID: String): WifiConfiguration? {
        for (configuration in wifiManager.configuredNetworks) {
            // 注意的是，SSID是一个加了双引号的字符串
            if (targetSSID == configuration.getParseSSID()) {
                return configuration
            }
        }
        return null
    }

    /**
     * 从int值解析格式化的ipv4地址
     */
    private fun WifiInfo.getParseIp(): String {
        return String.format("%d.%d.%d.%d", ipAddress and 0xff, ipAddress shr 8 and 0xff,
            ipAddress shr 16 and 0xff, ipAddress shr 24 and 0xff)
    }

    /**
     * 获取解析后的wifi名，默认的wifi名两侧有双引号
     */
    private fun WifiConfiguration.getParseSSID(): String {
        return getParseSSID(SSID)
    }

    /**
     * 获取解析后的wifi名，默认的wifi名两侧有双引号
     */
    private fun WifiInfo.getParseSSID(): String {
        return getParseSSID(ssid)
    }

    private fun getParseSSID(ssid: String): String {
        if (TextUtils.isEmpty(ssid)) return ""
        val matcher = Pattern.compile("^\"(.*)\"$").matcher(ssid)
        if (matcher.find())
            return matcher.group(1) ?: ssid
        return ssid
    }

    /**
     * 生成一个新的wifi配置
     *
     * @param targetSSID wifi名
     * @param psd 密码
     */
    private fun generateNewWifiConfiguration(targetSSID: String, psd: String): WifiConfiguration? {
        return WifiConfiguration().apply {
            // wifi名两侧要有双引号
            SSID = "\"$targetSSID\""
            // WPA网络
            preSharedKey = "\"$psd\""
        }
    }

    /**
     * 连接到指定的wifi
     */
    fun connectToWifi(wifiManager: WifiManager, ssid: String, psd: String) {
        LogUtil.i("try connect to $ssid")
        // 查找扫描到的wifi
        val scannedWifi = findInScannedWifi(wifiManager, ssid)
        LogUtil.d("scanned targetWifi = ${scannedWifi?.SSID}")
        // 查找配置过的wifi
        val wifiConfiguration = findInConfiguredWifi(wifiManager, ssid)
        LogUtil.d("configured targetWifi = ${wifiConfiguration?.getParseSSID()}")
        // 当前连接的wifi
        val currentWifi = wifiManager.connectionInfo
        LogUtil.d("currentWifi = ${currentWifi.getParseSSID()}")
        // 当前IP地址
        LogUtil.i("currentIp = ${currentWifi.getParseIp()}")
        if (scannedWifi == null) {
            LogUtil.e("cannot find target wifi $ssid, return")
            return
        }
        if (wifiConfiguration != null) {
            // 已经配置过这个网络
            if (currentWifi.getParseSSID() == ssid) {
                LogUtil.d("already connect $ssid")
            } else{
                LogUtil.i("connecting $ssid")
                val result = wifiManager.enableNetwork(wifiConfiguration.networkId, true)
                LogUtil.i("connecting success = $result")
            }
        } else {
            // 这个网络还没有配置过
            var newConfiguration = generateNewWifiConfiguration(ssid, psd)
            LogUtil.d("generate newWifiConfiguration ${newConfiguration?.getParseSSID()}")
            wifiManager.addNetwork(newConfiguration)
            // 连接这个网络，这里不会阻塞，当网络连接成功后，NETWORK_STATE_CHANGED_ACTION广播会被发送，在那里判断是否获得了正确的ip地址
            newConfiguration = findInConfiguredWifi(wifiManager, ssid)
            val result = wifiManager.enableNetwork(newConfiguration!!.networkId, true)
            LogUtil.e("connected newWifiConfiguration success = $result")
        }

    }

}