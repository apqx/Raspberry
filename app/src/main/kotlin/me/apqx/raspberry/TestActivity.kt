package me.apqx.raspberry

import android.os.Bundle
import android.os.SystemClock
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import me.apqx.logutil.LogUtil
import me.apqx.raspberry.databinding.ActivityTestBinding

class TestActivity : AppCompatActivity() {
    lateinit var dataBinding: ActivityTestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_test)
    }

    var i = 0

    fun onClick(view: View) {
        when (view.id) {
            R.id.btn_start -> {
                LogUtil.d("test log $i++")
            }

            R.id.btn_stop -> {
                LogUtil.close()
            }
        }
    }
}