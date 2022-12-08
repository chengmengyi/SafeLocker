package com.demo.safelocker.admob

import com.demo.safelocker.app.mSafeApp
import com.demo.safelocker.app.safeLog
import com.demo.safelocker.entity.AdmobEntity
import com.demo.safelocker.entity.AdmobResultEntity
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAdOptions

abstract class LoadAdmob {
    fun loadAdByType(adType: String,admobEntity: AdmobEntity,result:(entity:AdmobResultEntity?)->Unit){
        when(admobEntity.slk_tt){
            "open"->{
                AppOpenAd.load(
                    mSafeApp,
                    admobEntity.slk_ii,
                    AdRequest.Builder().build(),
                    AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                    object : AppOpenAd.AppOpenAdLoadCallback(){
                        override fun onAdLoaded(p0: AppOpenAd) {
                            safeLog("load $adType success")
                            result.invoke(AdmobResultEntity(admob = p0, time = System.currentTimeMillis()))
                        }

                        override fun onAdFailedToLoad(p0: LoadAdError) {
                            super.onAdFailedToLoad(p0)
                            safeLog("load $adType error,${p0.message}")
                            result.invoke(null)
                        }
                    }
                )
            }
            "inter"->{
                InterstitialAd.load(
                    mSafeApp,
                    admobEntity.slk_ii,
                    AdRequest.Builder().build(),
                    object : InterstitialAdLoadCallback(){
                        override fun onAdFailedToLoad(p0: LoadAdError) {
                            safeLog("load $adType error,${p0.message}")
                            result.invoke(null)
                        }

                        override fun onAdLoaded(p0: InterstitialAd) {
                            safeLog("load $adType success")
                            result.invoke(AdmobResultEntity(admob = p0, time = System.currentTimeMillis()))                        }
                    }
                )
            }
            "native"->{
                AdLoader.Builder(
                    mSafeApp,
                    admobEntity.slk_ii,
                ).forNativeAd { p0->
                    safeLog("load $adType success")
                    result.invoke(AdmobResultEntity(admob = p0, time = System.currentTimeMillis()))
                }
                    .withAdListener(object : AdListener(){
                        override fun onAdFailedToLoad(p0: LoadAdError) {
                            super.onAdFailedToLoad(p0)
                            safeLog("load $adType error,${p0.message}")
                            result.invoke(null)
                        }
                    })
                    .withNativeAdOptions(
                        NativeAdOptions.Builder()
                            .setAdChoicesPlacement(
                                NativeAdOptions.ADCHOICES_BOTTOM_LEFT
                            )
                            .build()
                    )
                    .build()
                    .loadAd(AdRequest.Builder().build())
            }
        }
    }
}