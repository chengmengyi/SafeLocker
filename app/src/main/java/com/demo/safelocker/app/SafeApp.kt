package com.demo.safelocker.app

import android.app.Application
import com.demo.safelocker.config.SafeFirebase
import com.tencent.mmkv.MMKV

lateinit var mSafeApp: SafeApp
class SafeApp:Application() {
    override fun onCreate() {
        super.onCreate()
        mSafeApp=this
        MMKV.initialize(this)
        RegisterActivity.register(this)
        SafeFirebase.readFirebase()
    }
}