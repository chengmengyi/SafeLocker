package com.demo.safelocker.app

import android.app.ActivityManager
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import com.demo.safelocker.config.SafeLocalConfig


fun View.show(boolean: Boolean){
    visibility=if (boolean) View.VISIBLE else View.GONE
}

fun Context.hasAppPer(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        try {
            val packageManager = packageManager
            val info = packageManager.getApplicationInfo(packageName, 0)
            val appOpsManager = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            appOpsManager.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                info.uid,
                info.packageName
            )
            appOpsManager.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                info.uid,
                info.packageName
            ) == AppOpsManager.MODE_ALLOWED
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    } else {
        true
    }
}

fun Context.isNoOption(): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val packageManager = packageManager
        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
        val list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        return list.size > 0
    }
    return false
}

fun hasOverPermission(context: Context): Boolean {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return true
    return Settings.canDrawOverlays(context)
}

fun isServiceRunning(className: String, context: Context): Boolean {
    //进程管理者
    val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    //获取进程中正在运行的服务集合
    val runningservice = manager.getRunningServices(1000)
    //遍历
    for (runningServiceInfo in runningservice) {
        //获取正在运行的服务的标识
        val service = runningServiceInfo.service
        //获取正在运行的服务的全类型
        val name = service.className
        //判断
        if (name == className) {
            return true
        }
    }
    return false
}


fun Context.contactUs(){
    try {
        val uri = Uri.parse("mailto:${SafeLocalConfig.email}")
        val intent = Intent(Intent.ACTION_SENDTO, uri)
        startActivity(intent)
    }catch (e: java.lang.Exception){
        toast("Contact us by email：${SafeLocalConfig.email}")
    }
}

fun Context.toast(string: String){
    Toast.makeText(this,string, Toast.LENGTH_LONG).show()
}

fun Context.shareApp(){
    val pm = packageManager
    val packageName=pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES).packageName
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.putExtra(
        Intent.EXTRA_TEXT,
        "https://play.google.com/store/apps/details?id=${packageName}"
    )
    startActivity(Intent.createChooser(intent, "share"))
}

fun Context.updateApp(){
    val packName = packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES).packageName
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse("https://play.google.com/store/apps/details?id=$packName")
    }
    startActivity(intent)
}