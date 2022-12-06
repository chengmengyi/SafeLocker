package com.demo.safelocker.base

import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import com.gyf.immersionbar.ImmersionBar

abstract class BaseAc:AppCompatActivity() {
    lateinit var immersionBar: ImmersionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fit()
        setContentView(layoutId())
        immersionBar= ImmersionBar.with(this).apply {
            statusBarAlpha(0f)
            autoDarkModeEnable(true)
            statusBarDarkFont(true)
            init()
        }
        view()
    }

    abstract fun layoutId():Int

    abstract fun view()

    private fun fit(){
        val metrics: DisplayMetrics = resources.displayMetrics
        val td = metrics.heightPixels / 760f
        val dpi = (160 * td).toInt()
        metrics.density = td
        metrics.scaledDensity = td
        metrics.densityDpi = dpi
    }
}