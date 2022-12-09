package com.demo.safelocker.app

import android.app.Application
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.tencent.mmkv.MMKV

class SafeApp:Application() {
    override fun onCreate() {
        super.onCreate()
        MMKV.initialize(this)
        Firebase.initialize(this)
    }
}