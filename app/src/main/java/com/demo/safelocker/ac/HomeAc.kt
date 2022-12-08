package com.demo.safelocker.ac

import android.content.Intent
import android.provider.Settings
import android.view.Gravity
import com.demo.safelocker.R
import com.demo.safelocker.ac.lock.SetPwdAc
import com.demo.safelocker.admob.AdType
import com.demo.safelocker.admob.RefreshAdmob
import com.demo.safelocker.admob.ShowNativeAdmob
import com.demo.safelocker.app.*
import com.demo.safelocker.base.BaseAc
import com.demo.safelocker.dialog.NoticeDialog
import com.demo.safelocker.lock.AppListManager
import com.demo.safelocker.lock.LockPwdManager
import com.demo.safelocker.lock.SetPwdEnum
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.layout_drawer.*
import kotlinx.android.synthetic.main.layout_home_content.*

class HomeAc:BaseAc() {
    private val showHomeAdmob by lazy { ShowNativeAdmob(this,AdType.HOME) }
    private val tips="To use the application lock function, you need to obtain the permission of \"View Application Usage\""

    override fun layoutId(): Int = R.layout.activity_home

    override fun view() {
        immersionBar.statusBarView(top).init()
        setClick()
    }

    private fun jumpSetPwd(){
        val intent = Intent(this, SetPwdAc::class.java).apply {
            putExtra(
                "type",
                if (LockPwdManager.hasPwd()) SetPwdEnum.CHECK_PWD else SetPwdEnum.SET_PWD
            )
        }
        startActivity(intent)
    }

    private fun setClick(){
        iv_set.setOnClickListener {
            if(!drawer_layout.isOpen){
                drawer_layout.openDrawer(Gravity.LEFT)
            }
        }
        view_lock.setOnClickListener {
            if(drawer_layout.isOpen) return@setOnClickListener
            if(!hasAppPer()&&isNoOption()){
                NoticeDialog(tips){
                    val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                    startActivityForResult(intent, 100)
                }.show(supportFragmentManager,"LookAppPermission")
            }else{
                AppListManager.scanAppList(this)
                jumpSetPwd()
            }
        }

        llc_contact.setOnClickListener {
            contactUs()
            drawer_layout.close()
        }

        llc_share.setOnClickListener {
            shareApp()
            drawer_layout.close()
        }

        llc_update.setOnClickListener {
            updateApp()
            drawer_layout.close()
        }

        llc_privacy.setOnClickListener {
            startActivity(Intent(this,UrlAc::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        if(RefreshAdmob.checkRefresh(AdType.HOME)){
            showHomeAdmob.loopCheckAdmob()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        showHomeAdmob.stopLoop()
    }
}