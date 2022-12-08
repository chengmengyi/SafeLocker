package com.demo.safelocker.lock

import com.tencent.mmkv.MMKV

object LockPwdManager {
    var failNum=3
    var lockUnlockNum=0

    fun showFullAd()=lockUnlockNum%5==0

    fun countFailNum(){
        if(failNum>0){
            failNum--
        }
    }

    fun hasPwd()=lockPwd()?.isNotEmpty()?:false

    fun writePwdToLocal(pwd:String){
        MMKV.defaultMMKV().encode("lock_pwd",pwd)
    }

    fun checkPwd(pwd:String)= lockPwd()==pwd

    fun reset(){
        writePwdToLocal("")
        failNum=3
        AppListManager.reset()
    }

    private fun lockPwd()=MMKV.defaultMMKV().decodeString("lock_pwd","")
}