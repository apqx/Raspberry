package me.apqx.logutil

import android.util.Log
import java.io.File

/**
 * 日志工具，支持将日志信息写入指定的本地文件
 */
object LogUtil {

    /**
     * 在Application中执行初始化
     *
     * @param dirFile 日志文件保存路径，必须确保权限，否则抛出异常
     */
    public fun init(dirFile: File) {

    }


    /**
     * 初始化日志工具，指定日志文件存储路径，默认是外部私有存储，无需请求存储权限
     */
    public fun init(dirPath: String) {

    }

    public fun d(tag: String, log: String) {
        Log.d(tag, log)
    }

    public fun e(tag: String, log: String) {
        Log.e(tag, log)
    }

    public fun i(tag: String, log: String) {
        Log.i(tag, log)
    }

    public fun v(tag: String, log: String) {
        Log.v(tag, log)
    }
}