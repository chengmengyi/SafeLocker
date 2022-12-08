package com.demo.safelocker.entity

class AdmobResultEntity(
    val admob:Any?=null,
    val time:Long=0L
) {
    fun isExpired()=(System.currentTimeMillis() - time) >=3600000L
}