package com.demo.safelocker.ac

import android.animation.ValueAnimator
import android.content.Intent
import android.view.KeyEvent
import android.view.animation.LinearInterpolator
import androidx.core.animation.doOnEnd
import com.blankj.utilcode.util.ActivityUtils
import com.demo.safelocker.R
import com.demo.safelocker.base.BaseAc
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseAc() {
    private var valueAnimator:ValueAnimator?=null

    override fun layoutId(): Int = R.layout.activity_main

    override fun view() {
        start()
    }

    private fun start(){
        valueAnimator=ValueAnimator.ofInt(0, 100).apply {
            duration = 3000L
            interpolator = LinearInterpolator()
            addUpdateListener {
                val progress = it.animatedValue as Int
                pb_progress.progress = progress
            }
            doOnEnd { jumpHome() }
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