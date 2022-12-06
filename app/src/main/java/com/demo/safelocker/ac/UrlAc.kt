package com.demo.safelocker.ac

import com.demo.safelocker.R
import com.demo.safelocker.base.BaseAc
import com.demo.safelocker.config.SafeLocalConfig
import kotlinx.android.synthetic.main.activity_url.*

class UrlAc:BaseAc() {
    override fun layoutId(): Int = R.layout.activity_url

    override fun view() {
        immersionBar.statusBarView(top).init()
        back.setOnClickListener { finish() }

        webview.apply {
            settings.javaScriptEnabled=true
            loadUrl(SafeLocalConfig.url)
        }
    }
}