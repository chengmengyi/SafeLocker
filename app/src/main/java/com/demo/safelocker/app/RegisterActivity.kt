package com.demo.safelocker.app

import android.app.Activity
import android.app.Application
import android.os.Bundle

object RegisterActivity {
    var closeAppAc=true

    fun register(application: Application){
        application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks{
            private var pages=0
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

            override fun onActivityStarted(activity: Activity) {
                pages++
            }

            override fun onActivityResumed(activity: Activity) {}

            override fun onActivityPaused(activity: Activity) {}

            override fun onActivityStopped(activity: Activity) {
                pages--
                if (pages<=0){

                }
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

            override fun onActivityDestroyed(activity: Activity) {}
        })
    }
}