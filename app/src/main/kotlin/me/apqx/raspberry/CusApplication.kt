package me.apqx.raspberry

import android.app.Application
import me.apqx.logutil.LogUtil
import java.io.File

class CusApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        LogUtil.init(getExternalFilesDir(null) as File)
    }
}