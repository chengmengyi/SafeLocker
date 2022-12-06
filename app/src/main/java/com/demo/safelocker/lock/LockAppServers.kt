package com.demo.safelocker.lock

import android.app.*
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import androidx.core.app.NotificationCompat
import com.demo.safelocker.R
import kotlinx.coroutines.*

class LockAppServers: Service() {
    private var showingAppName=""
    private val notificationId = "safelock"

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        CheckPwdView.createView(this)
//        startForeground(30000,getNotification())
        loopCheckTopApp()
    }

    private fun loopCheckTopApp(){
        GlobalScope.launch{
            while (true){
                delay(200)
                val name = getTopAppName()
                withContext(Dispatchers.Main){
                    if (!name.isNullOrEmpty()){
                        if (name!=showingAppName){
                            if (AppListManager.appIsLocked(name)&& Settings.canDrawOverlays(this@LockAppServers)){
                                CheckPwdView.showOverlay()
                            }else{
                                CheckPwdView.hideOverlay()
                            }
                        }
                        showingAppName=name
                    }
                }
            }
        }
    }

    private fun getTopAppName(): String {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            val activityManager=getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val appTasks = activityManager.getRunningTasks(1)
            if (null != appTasks && !appTasks.isEmpty()) {
                return appTasks[0].topActivity!!.packageName
            }
        } else {
            //5.0以后需要用这方法
            val sUsageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
            val endTime = System.currentTimeMillis()
            val beginTime = endTime - 10000
            var result = ""
            val event = UsageEvents.Event()
            val usageEvents = sUsageStatsManager.queryEvents(beginTime, endTime)
            while (usageEvents.hasNextEvent()) {
                usageEvents.getNextEvent(event)
                if (event.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                    result = event.packageName
                }
            }
            if (!TextUtils.isEmpty(result)) {
                return result
            }
        }
        return ""
    }

    private  fun getNotification(): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(notificationId, notificationId, NotificationManager.IMPORTANCE_HIGH)
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).run {
                if (getNotificationChannel(notificationId) == null) createNotificationChannel(channel)
            }
        }
        val builder = NotificationCompat.Builder(this,notificationId)
            .setSmallIcon(R.mipmap.ic_launcher_foreground)
            .setContentTitle(getString(R.string.app_name))
            .setContentText("Running")
        return builder.build()
    }
}