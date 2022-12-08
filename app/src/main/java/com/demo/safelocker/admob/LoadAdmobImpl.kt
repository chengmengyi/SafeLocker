package com.demo.safelocker.admob

import com.demo.safelocker.app.safeLog
import com.demo.safelocker.config.SafeFirebase
import com.demo.safelocker.entity.AdmobEntity
import com.demo.safelocker.entity.AdmobResultEntity
import org.json.JSONObject

object LoadAdmobImpl :LoadAdmob() {
    private val loadingAdmob= arrayListOf<String>()
    private val resultAdmobMap= hashMapOf<String,AdmobResultEntity>()

    fun preLoad(adType: String,openAdAgain:Boolean=true){
        if(checkLoading(adType)){
            safeLog("$adType loading")
            return
        }
        if(checkHasCache(adType)){
            safeLog("$adType has cache")
            return
        }
        val parseAdmobJson = parseAdmobJson(adType)
        if(parseAdmobJson.isEmpty()){
            safeLog("$adType admob json empty")
            return
        }
        loopLoadAd(adType,parseAdmobJson.iterator(),openAdAgain)
    }

    private fun loopLoadAd(adType: String, iterator: Iterator<AdmobEntity>, openAdAgain: Boolean){
        loadAdByType(adType,iterator.next()){
            if(it==null){
                if(iterator.hasNext()){
                    loopLoadAd(adType,iterator,openAdAgain)
                }else{
                    loadingAdmob.remove(adType)
                    if(openAdAgain&&adType==AdType.OPEN){
                        preLoad(adType,openAdAgain = false)
                    }
                }
            }else{
                loadingAdmob.remove(adType)
                resultAdmobMap[adType]=it
            }
        }
    }

    private fun parseAdmobJson(adType: String):List<AdmobEntity>{
        val list= arrayListOf<AdmobEntity>()
        try {
            val jsonArray = JSONObject(SafeFirebase.readAdmobJson()).getJSONArray(adType)
            for (index in 0 until jsonArray.length()){
                val jsonObject = jsonArray.getJSONObject(index)
                list.add(
                    AdmobEntity(
                        jsonObject.optString("slk_tt"),
                        jsonObject.optInt("slk_ww"),
                        jsonObject.optString("slk_ss"),
                        jsonObject.optString("slk_ii"),
                    )
                )
            }
        }catch (e:Exception){
        }
        return list.filter { it.slk_ss == "admob" }.sortedByDescending { it.slk_ww }
    }

    private fun checkLoading(adType: String)= loadingAdmob.contains(adType)

    private fun checkHasCache(adType: String):Boolean{
        if(resultAdmobMap.containsKey(adType)){
            val admobResultEntity = resultAdmobMap[adType]
            if(admobResultEntity?.admob!=null){
                if(admobResultEntity.isExpired()){
                    removeAdmob(adType)
                }else{
                    safeLog("$adType has cache")
                    return true
                }
            }
        }
        return false
    }


    fun removeAdmob(adType: String){
        resultAdmobMap.remove(adType)
    }

    fun getAdmob(adType: String)= resultAdmobMap[adType]?.admob
}