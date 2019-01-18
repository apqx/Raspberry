package me.apqx.logutil

import android.util.Log
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.LinkedBlockingQueue

private const val TAG = "apqx"
private const val TAG_SELF = "logUtil"
private const val LOG_QUEUE_CAPACITY = 100

/**
 * 日志工具，支持将日志信息写入指定的本地文件
 */
object LogUtil {
    private lateinit var dirFile: File
    private var fileWriter: FileWriter? = null
    private var logThread: Thread? = null
    private val logNameFormat = SimpleDateFormat("yyyyMMdd", Locale.CHINA)
    private val logTimeFormat = SimpleDateFormat.getTimeInstance()
    private val logQueue = LinkedBlockingQueue<String>(LOG_QUEUE_CAPACITY)

    /**
     * 在Application中执行初始化
     *
     * @param dirFile 日志文件保存路径，必须确保权限，否则抛出异常
     */
    fun init(dirFile: File) {
        this.dirFile = dirFile
        d(TAG_SELF, "init, logDir = ${dirFile.absolutePath}")
        checkLogThread()
    }

    /**
     * 检查写入日志的线程
     */
    private fun checkLogThread() {
        if (logThread == null || logThread!!.isInterrupted) {
            logThread = Thread {
                d(TAG_SELF, "start logThread $logThread")
                while (!logThread!!.isInterrupted) {
                    checkFile()
                    val log = logQueue.take()
                    fileWriter?.write(log)
                }
            }
        }
        logThread?.start()
    }

    /**
     * 检查日志文件是否存在，不存在即创建指向它的FileWriter
     */
    private fun checkFile() {
        val logFile = File(dirFile, "${logNameFormat.format(Date())}.log")
        if (fileWriter == null) {
            d(TAG_SELF, "logFileWriter init, logFile = ${logFile.absolutePath}")
            fileWriter = FileWriter(dirFile.absolutePath, logFile.name)
        }

        if (logFile.absolutePath != fileWriter?.logFile?.absolutePath) {
            d(TAG_SELF, "logFileWriter redirect, logFile = ${logFile.absolutePath}")
            fileWriter?.redirect(logFile)
        }
    }

    /**
     * 关闭日志工具
     */
    fun close() {
        d(TAG_SELF, "stop logThread $logThread")
        logThread?.interrupt()
        fileWriter?.close()
    }

    fun d(tag: String, log: String, saveToFile: Boolean = true) {
        Log.d(tag, log)
        if (saveToFile) {
            saveQueue(tag, log)
        }
    }

    fun d(log: String, saveToFile: Boolean = true) {
        d(TAG, log)
    }

    fun e(tag: String, log: String, saveToFile: Boolean = true) {
        Log.e(tag, log)
        if (saveToFile) {
            saveQueue(tag, log)
        }
    }

    fun e(log: String, saveToFile: Boolean = true) {
        e(TAG, log)
    }

    fun i(tag: String, log: String, saveToFile: Boolean = true) {
        Log.i(tag, log)
        if (saveToFile) {
            saveQueue(tag, log)
        }
    }

    fun i(log: String, saveToFile: Boolean = true) {
        i(TAG, log)
    }

    fun v(tag: String, log: String, saveToFile: Boolean = true) {
        Log.v(tag, log)
        if (saveToFile) {
            saveQueue(tag, log)
        }
    }

    fun v(log: String, saveToFile: Boolean = true) {
        v(TAG, log)
    }

    /**
     * 将要写入文件的日志存入暂存队列
     */
    private fun saveQueue(tag: String, log: String) {
        if (logQueue.size == LOG_QUEUE_CAPACITY) {
            logQueue.clear()
            logQueue.put(generateLog(TAG_SELF, "logQueue.size = $LOG_QUEUE_CAPACITY, clear all old log"))
        }
        logQueue.put(generateLog(tag, log))
    }

    /**
     * 生成带时间戳的格式化日志
     */
    private fun generateLog(tag: String, log: String): String {
        return "${logTimeFormat.format(Date())} : $tag = $log \n"
    }

}