package com.demo.safelocker.admob

import android.app.Activity
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

class ShowLockAdmob(
    private val activity: Activity,
    private val adType: String
) {

    fun showFullAd(){
        val admob = LoadAdmobImpl.getAdmob(adType)
        if(null!=admob){
            safeLog("start show $adType")
            if(admob is AppOpenAd){
                admob.fullScreenContentCallback=fullScreenContentCallback
                admob.show(activity)
            }
            if(admob is InterstitialAd){
                admob.fullScreenContentCallback=fullScreenContentCallback
                admob.show(activity)
            }
        }else{
            LoadAdmobImpl.preLoad(adType)
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
        }
    }
}