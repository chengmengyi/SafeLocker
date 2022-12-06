//package com.sxx.secureapplock.view
//
//import android.app.*
//import android.app.usage.UsageEvents
//import android.app.usage.UsageStatsManager
//import android.content.Context
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.graphics.PixelFormat
//import android.os.Build
//import android.os.IBinder
//import android.provider.Settings
//import android.view.WindowManager
//import androidx.core.app.NotificationCompat
//import com.blankj.utilcode.util.Utils
//import com.demo.safelocker.R
//import com.sxx.secureapplock.R
//import com.sxx.secureapplock.utils.Cache
//import com.sxx.secureapplock.utils.Utils
//import kotlinx.coroutines.*
//import java.util.jar.Manifest
//
//class LockService : Service() {
//
//    override fun onBind(intent: Intent) = null
//    private lateinit var manager: WindowManager
//    private val layoutParams = getWindowLayoutParams()
//    private var currentName = ""
//    lateinit var passwordView: PasswordView
//    private var isShow = false
//    override fun onCreate() {
//        resources.displayMetrics.run {
//            (heightPixels / 760f).run {
//                density = this
//                scaledDensity = this
//                densityDpi = (this * 160).toInt()
//            }
//        }
//        super.onCreate()
//        startForeground(30000,getNotification())
//        manager = getSystemService(WINDOW_SERVICE) as WindowManager
//        GlobalScope.launch(Dispatchers.IO) {
//            while (true) {
//                delay(350)
//                withContext(Dispatchers.Main) {
//                    getAppPackageName(this@LockService).run {
//                        if (isNotEmpty()) {
//                            if (this != currentName) {
//                                if (isPermission() && Cache.lockContent.contains(this) && isShow.not()) {
//                                    isShow = true
//                                    passwordView = PasswordView(this@LockService).apply {
//                                        setSuccess { goneView() }
//                                    }
//                                    manager.addView(passwordView, layoutParams)
//                                } else goneView()
//                            }
//                            currentName = this
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private fun goneView() {
//        if (isShow) {
//            isShow = false
//            manager.removeViewImmediate(passwordView)
//        }
//    }
//
//
//    private fun isPermission(): Boolean {
//        return Settings.canDrawOverlays(this@LockService) && Utils.isSage(this)
//    }
//
//
//    private fun getWindowLayoutParams() = WindowManager.LayoutParams(
//        WindowManager.LayoutParams.MATCH_PARENT,
//        WindowManager.LayoutParams.MATCH_PARENT,
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
//        } else {
//            WindowManager.LayoutParams.TYPE_PHONE
//        },
//        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH or
//                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
//                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
//                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//        PixelFormat.TRANSLUCENT
//    )
//
//    private val notificationId = "x200"
//
//    private  fun getNotification(): Notification {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel =
//                NotificationChannel(notificationId, notificationId, NotificationManager.IMPORTANCE_HIGH)
//            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).run {
//                if (getNotificationChannel(notificationId) == null) createNotificationChannel(channel)
//            }
//        }
//        val builder = NotificationCompat.Builder(this,notificationId)
//            .setSmallIcon(R.mipmap.ic_launcher_foreground)
//            .setContentTitle(getString(R.string.app_name))
//            .setContentText("Running")
//        return builder.build()
//    }
//
//}