package com.demo.safelocker.lock

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.util.ArraySet
import android.util.Log
import com.demo.safelocker.entity.AppInfoEntity
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object AppListManager {
    private val lockedAppPackageNameList = arrayListOf<String>()
    val unlockList= arrayListOf<AppInfoEntity>()
    val lockedList= arrayListOf<AppInfoEntity>()

    fun scanAppList(context: Context){
        getLockedAppPackageNameList()
        val packageName = context.packageName
        GlobalScope.launch {
            val packageManager: PackageManager = context.packageManager
            val list = packageManager.getInstalledPackages(0)
            lockedList.clear()
            unlockList.clear()
            for (packageInfo in list) {
                val packageN = packageInfo.applicationInfo.packageName
                val bean = AppInfoEntity(
                    icon = packageInfo.applicationInfo.loadIcon(packageManager),
                    title = packageManager.getApplicationLabel(packageInfo.applicationInfo).toString(),
                    packageName = packageN,
                    locked = lockedAppPackageNameList.contains(packageN)
                )
                val flags = packageInfo.applicationInfo.flags
                if (flags and ApplicationInfo.FLAG_SYSTEM != 0) {

                } else {
                    if (bean.packageName!=packageName){
                        if (bean.locked){
//                            bean.index=lockedAppPackageNameList.indexOf(bean.packageName)
                            lockedList.add(bean)
                        }else{
                            unlockList.add(bean)
                        }
                    }
                }
            }
        }
    }

    private fun getLockedAppPackageNameList(){
        val app = MMKV.defaultMMKV().decodeString("locked")?:""
        if (!app.isNullOrEmpty()){
            lockedAppPackageNameList.clear()
            lockedAppPackageNameList.addAll(app.split("|").toMutableList())
        }
    }

    fun lockOrUnlock(appInfoEntity: AppInfoEntity){
        if(appInfoEntity.locked){
            appInfoEntity.locked=!appInfoEntity.locked
            lockedAppPackageNameList.remove(appInfoEntity.packageName)
            lockedList.remove(appInfoEntity)
            unlockList.add(0,appInfoEntity)
        }else{
            appInfoEntity.locked=!appInfoEntity.locked
            lockedAppPackageNameList.add(0,appInfoEntity.packageName)
            lockedList.add(0,appInfoEntity)
            unlockList.remove(appInfoEntity)
        }
        saveLockedAppPackageNameList()
    }

    fun appIsLocked(name:String)=lockedAppPackageNameList.contains(name)

    private fun saveLockedAppPackageNameList(){
        val stringBuffer=StringBuffer()
        lockedAppPackageNameList.forEach {
            stringBuffer.append("$it|")
        }
        MMKV.defaultMMKV().encode("locked",stringBuffer.toString().replaceAfterLast("|",""))
    }

    fun reset(){
        lockedAppPackageNameList.clear()
        saveLockedAppPackageNameList()
        unlockList.addAll(lockedList)
        unlockList.forEach {
            it.locked=false
        }
        lockedList.clear()
    }
}