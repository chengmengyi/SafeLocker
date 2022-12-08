package com.demo.safelocker.app

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import com.blankj.utilcode.util.ActivityUtils
import com.demo.safelocker.ac.HomeAc
import com.demo.safelocker.ac.MainActivity
import com.demo.safelocker.admob.RefreshAdmob
import com.google.android.gms.ads.AdActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object RegisterActivity {
    var closeAppAc=true

    var appFront=true
    private var toMain=false
    private var job: Job?=null


    fun register(application: Application){
        application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks{
            private var pages=0
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

            override fun onActivityStarted(activity: Activity) {
                pages++
                job?.cancel()
                job=null
                if (pages==1){
                    appFront=true
                    if (toMain){
                        if (ActivityUtils.isActivityExistsInStack(HomeAc::class.java)){
                            activity.startActivity(Intent(activity, MainActivity::class.java))
                        }
                    }
                    toMain=false
                }
            }

            override fun onActivityResumed(activity: Activity) {}

            override fun onActivityPaused(activity: Activity) {}

            override fun onActivityStopped(activity: Activity) {
                pages--
                if (pages<=0){
                    appFront=false
                    RefreshAdmob.reset()
                    job= GlobalScope.launch {
                        delay(3000L)
                        toMain=true
                        ActivityUtils.finishActivity(MainActivity::class.java)
                        ActivityUtils.finishActivity(AdActivity::class.java)
                    }
                }
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

            override fun onActivityDestroyed(activity: Activity) {}
        })
    }
}