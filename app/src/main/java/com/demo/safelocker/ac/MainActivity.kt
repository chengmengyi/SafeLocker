package com.demo.safelocker.ac

import android.animation.ValueAnimator
import android.content.Intent
import android.view.KeyEvent
import android.view.animation.LinearInterpolator
import androidx.core.animation.doOnEnd
import com.blankj.utilcode.util.ActivityUtils
import com.demo.safelocker.R
import com.demo.safelocker.admob.AdType
import com.demo.safelocker.admob.LoadAdmobImpl
import com.demo.safelocker.admob.ShowFullAdmob
import com.demo.safelocker.base.BaseAc
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseAc() {
    private var valueAnimator:ValueAnimator?=null
    private val showOpenAd by lazy { ShowFullAdmob(this,AdType.OPEN){ jumpHome() } }

    override fun layoutId(): Int = R.layout.activity_main

    override fun view() {
        LoadAdmobImpl.preLoad(AdType.OPEN)
        LoadAdmobImpl.preLoad(AdType.HOME)
        LoadAdmobImpl.preLoad(AdType.LOCK)

        start()
    }

    private fun start(){
        valueAnimator=ValueAnimator.ofInt(0, 100).apply {
            duration = 10000L
            interpolator = LinearInterpolator()
            addUpdateListener {
                val progress = it.animatedValue as Int
                pb_progress.progress = progress
                val pro = (10 * (progress / 100.0F)).toInt()
                if (pro in 2..9){
                    showOpenAd.showFullAd{
                        pb_progress.progress = 100
                        stop()
                    }
                }else if (pro>=10){
                    jumpHome()
                }
            }
            start()
        }
    }

    private fun jumpHome(){
        if (!ActivityUtils.isActivityExistsInStack(HomeAc::class.java)){
            startActivity(Intent(this,HomeAc::class.java))
        }
        finish()
    }

    private fun stop(){
        valueAnimator?.removeAllUpdateListeners()
        valueAnimator?.cancel()
        valueAnimator=null
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode== KeyEvent.KEYCODE_BACK){
            return true
        }
        return false
    }

    override fun onResume() {
        super.onResume()
        valueAnimator?.resume()
    }

    override fun onPause() {
        super.onPause()
        valueAnimator?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        stop()
    }
}