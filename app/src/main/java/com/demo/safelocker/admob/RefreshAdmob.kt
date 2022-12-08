package com.demo.safelocker.admob

object RefreshAdmob {
    var fullAdmobShowing=false
    private val refreshTag= hashMapOf<String,Boolean>()

    fun checkRefresh(adType: String)= refreshTag[adType]?:true

    fun setRefreshTag(adType: String,tag:Boolean){
        refreshTag[adType]=tag
    }

    fun reset(){
        refreshTag.keys.forEach {
            refreshTag[it]=true
        }
    }
}