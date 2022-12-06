package com.demo.safelocker.app

import android.app.Application
import com.tencent.mmkv.MMKV

class SafeApp:Application() {
    override fun onCreate() {
        super.onCreate()
        MMKV.initialize(this)
    }
}