package com.demo.safelocker.config

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.tencent.mmkv.MMKV

object SafeFirebase {

    fun readFirebase(){
//        val remoteConfig = Firebase.remoteConfig
//        remoteConfig.fetchAndActivate().addOnCompleteListener {
//            if (it.isSuccessful){
//                saveAdmobJson(remoteConfig.getString("slk_mainos"))
//            }
//        }
    }

    private fun saveAdmobJson(string: String) {
        MMKV.defaultMMKV().encode("slk_mainos",string)
    }

    fun readAdmobJson():String{
        val s = MMKV.defaultMMKV().decodeString("slk_mainos") ?: ""
        if(s.isEmpty()){
            return SafeLocalConfig.ad
        }
        return s
    }
}