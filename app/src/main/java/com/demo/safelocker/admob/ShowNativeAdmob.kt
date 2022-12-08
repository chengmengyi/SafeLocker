package com.demo.safelocker.admob

import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.utils.widget.ImageFilterView
import com.blankj.utilcode.util.SizeUtils
import com.demo.safelocker.R
import com.demo.safelocker.app.show
import com.demo.safelocker.base.BaseAc
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import kotlinx.coroutines.*

class ShowNativeAdmob(
    private val baseAc: BaseAc,
    private val adType: String
) {
    
    private var loopJob:Job?=null
    private var lastAdmob:NativeAd?=null

    fun loopCheckAdmob(){
        LoadAdmobImpl.preLoad(adType)
        stopLoop()
        loopJob= GlobalScope.launch(Dispatchers.Main) {
            delay(300L)
            if (!baseAc.resume){
                return@launch
            }
            while (true) {
                if (!isActive) {
                    break
                }

                val admob = LoadAdmobImpl.getAdmob(adType)
                if(baseAc.resume && null!=admob && admob is NativeAd){
                    cancel()
                    lastAdmob?.destroy()
                    lastAdmob=admob
                    showNativeAd(admob)
                }

                delay(1000L)
            }
        }
    }
    
    private fun showNativeAd(admob: NativeAd){
        val viewNative = baseAc.findViewById<NativeAdView>(R.id.native_view)
        viewNative.iconView=baseAc.findViewById(R.id.native_logo)
        (viewNative.iconView as ImageFilterView).setImageDrawable(admob.icon?.drawable)

        viewNative.callToActionView=baseAc.findViewById(R.id.native_install)
        (viewNative.callToActionView as AppCompatTextView).text= admob.callToAction

        if(adType==AdType.HOME){
            viewNative.mediaView=baseAc.findViewById(R.id.native_cover)
            admob.mediaContent?.let {
                viewNative.mediaView?.apply {
                    setMediaContent(it)
                    setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                    outlineProvider = object : ViewOutlineProvider() {
                        override fun getOutline(view: View?, outline: Outline?) {
                            if (view == null || outline == null) return
                            outline.setRoundRect(
                                0,
                                0,
                                view.width,
                                view.height,
                                SizeUtils.dp2px(6F).toFloat()
                            )
                            view.clipToOutline = true
                        }
                    }
                }
            }
        }

        viewNative.bodyView=baseAc.findViewById(R.id.native_desc)
        (viewNative.bodyView as AppCompatTextView).text=admob.body

        viewNative.headlineView=baseAc.findViewById(R.id.native_title)
        (viewNative.headlineView as AppCompatTextView).text=admob.headline

        viewNative.setNativeAd(admob)
        viewNative.show(true)
        baseAc.findViewById<AppCompatImageView>(R.id.iv_cover).show(false)

        LoadAdmobImpl.removeAdmob(adType)
        LoadAdmobImpl.preLoad(adType)
        RefreshAdmob.setRefreshTag(adType,false)
    }
    
    fun stopLoop(){
        loopJob?.cancel()
        loopJob=null
    }
}