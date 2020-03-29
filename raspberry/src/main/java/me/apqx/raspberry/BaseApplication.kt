package me.apqx.raspberry

import android.app.Application
import android.content.Context
import me.apqx.jetlog.LogUtil

class BaseApplication : Application() {
    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        LogUtil.init(true)
    }
}