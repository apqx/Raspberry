package me.apqx.socket.tools

import java.lang.StringBuilder

object ByteTools {

    /**
     * 将字节数组转换为十六进制字符串显示
     */
    fun byteToHexString(byteArray: ByteArray): String {
        val stringBuilder = StringBuilder()
        for (byte in byteArray) {
            var item = Integer.toHexString(byte.toInt() and 0xff)
            if (item.length == 1) {
                item = "0$item"
            }
            stringBuilder.append(item)
                .append(" ")
        }
        val result: String = stringBuilder.toString().toUpperCase()
        if (result.length > 1) {
            return result.substring(0, result.length - 1)
        }
        return result
    }
}