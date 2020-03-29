package me.apqx.jetlog

import android.os.Handler
import android.util.Log
import java.lang.IllegalStateException

object LogUtil {
    private const val TAG = "apqx"
    private var showLog = false
    private lateinit var listenerSet: MutableSet<ILogUIListener>

    private lateinit var handler: Handler

    fun init(showLog: Boolean) {
        this.showLog = showLog
        handler = Handler()
    }

    fun registerUIListener(listener: ILogUIListener) {
        if (!LogUtil::listenerSet.isInitialized) {
            listenerSet = HashSet()
        }
        listenerSet.add(listener)
    }

    fun unregisterUIListener(listener: ILogUIListener) {
        listenerSet.remove(listener)
    }

    fun d(log: String) {
        d(TAG, log)
    }

    fun d(tag: String, log: String) {
        if (showLog()) {
            Log.d(tag, log)
            printLog2UI(log)
        }
    }

    private fun showLog(): Boolean {
        if (!LogUtil::handler.isInitialized) {
            throw IllegalStateException("LogUtil must be init!")
        }
        return showLog
    }

    fun e(log: String) {
        e(TAG, log)
    }

    fun e(tag: String, log: String) {
        if (showLog()) {
            Log.e(tag, log)
            printLog2UI(log)
        }
    }

    fun i(log: String) {
        i(TAG, log)
    }

    fun i(tag: String, log: String) {
        if (showLog()) {
            Log.i(tag, log)
            printLog2UI(log)
        }
    }

    private fun printLog2UI(log: String) {
        if (!LogUtil::listenerSet.isInitialized) return
        for (listener in listenerSet) {
            handler.post {
                listener.log(log)
            }
        }
    }
}