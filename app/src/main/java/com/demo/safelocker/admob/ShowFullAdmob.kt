package com.demo.safelocker.admob

import com.demo.safelocker.app.safeLog
import com.demo.safelocker.base.BaseAc
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ShowFullAdmob(
    private val baseAc: BaseAc,
    private val adType: String,
    private val closeAdmob:()->Unit
) {

    fun showFullAd(finish:(refresh:Boolean)->Unit){
        val admob = LoadAdmobImpl.getAdmob(adType)
        if(null!=admob){
            if(RefreshAdmob.fullAdmobShowing||!baseAc.resume){
                finish.invoke(false)
            }else{
                finish.invoke(false)
                safeLog("start show $adType")
                if(admob is AppOpenAd){
                    admob.fullScreenContentCallback=fullScreenContentCallback
                    admob.show(baseAc)
                }
                if(admob is InterstitialAd){
                    admob.fullScreenContentCallback=fullScreenContentCallback
                    admob.show(baseAc)
                }
            }
        }
    }

    private val fullScreenContentCallback=object : FullScreenContentCallback() {
        override fun onAdDismissedFullScreenContent() {
            super.onAdDismissedFullScreenContent()
            RefreshAdmob.fullAdmobShowing=false
            closeAdmob()
        }

        override fun onAdShowedFullScreenContent() {
            super.onAdShowedFullScreenContent()
            RefreshAdmob.fullAdmobShowing=true
            LoadAdmobImpl.removeAdmob(adType)
        }

        override fun onAdFailedToShowFullScreenContent(p0: AdError) {
            super.onAdFailedToShowFullScreenContent(p0)
            RefreshAdmob.fullAdmobShowing=false
            LoadAdmobImpl.removeAdmob(adType)
            closeAdmob()
        }

        private fun closeAdmob(){
            if (adType!=AdType.OPEN){
                LoadAdmobImpl.preLoad(adType)
            }
            GlobalScope.launch(Dispatchers.Main) {
                delay(200L)
                if (baseAc.resume){
                    closeAdmob.invoke()
                }
            }
        }
    }
}