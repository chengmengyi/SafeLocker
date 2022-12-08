package com.demo.safelocker.ac.lock

import android.content.Intent
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.demo.safelocker.R
import com.demo.safelocker.ac.HomeAc
import com.demo.safelocker.ac.lock.app_list.LockedFragment
import com.demo.safelocker.ac.lock.app_list.UnlockFragment
import com.demo.safelocker.admob.AdType
import com.demo.safelocker.admob.ShowNativeAdmob
import com.demo.safelocker.app.RegisterActivity
import com.demo.safelocker.app.isServiceRunning
import com.demo.safelocker.base.BaseAc
import com.demo.safelocker.interfaces.IUpdateAppListCallback
import com.demo.safelocker.lock.LockAppServers
import kotlinx.android.synthetic.main.activity_app.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.lang.Exception

class AppAc:BaseAc(),IUpdateAppListCallback{
    private val fragmentList= arrayListOf<Fragment>()

    override fun layoutId(): Int = R.layout.activity_app

    override fun view() {
        immersionBar.statusBarView(top).init()

        back.setOnClickListener { finish() }
        tv_locked_tab.setOnClickListener { view_pager.currentItem=0 }
        tv_unlock_tab.setOnClickListener { view_pager.currentItem=1 }

        setTabIndex(0)
        setAdapter()

        val serviceRunning = isServiceRunning(LockAppServers::class.java.name, this)
        if(!serviceRunning){
//            ActivityCompat.startForegroundService(this, Intent(this, LockAppServers::class.java))
            startService(Intent(this,LockAppServers::class.java))
        }

        EventBus.getDefault().post("")
    }

    private fun setAdapter(){
        fragmentList.add(LockedFragment())
        fragmentList.add(UnlockFragment())
        view_pager.adapter=object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getCount(): Int = fragmentList.size

            override fun getItem(position: Int): Fragment = fragmentList[position]
        }
        view_pager.addOnPageChangeListener(
            object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                }

                override fun onPageSelected(position: Int) {
                    setTabIndex(position)
                }

                override fun onPageScrollStateChanged(state: Int) {

                }

            }
        )
    }

    private fun setTabIndex(index:Int){
        tv_locked_tab.isSelected=index==0
        tv_unlock_tab.isSelected=index!=0
    }

    override fun updateAppList(locked: Boolean) {
        if (locked){
            try {
                val fragment = fragmentList[0]
                if(fragment is LockedFragment){
                    fragment.updateList()
                }
            }catch (e:Exception){

            }
        }else{
            try {
                val fragment = fragmentList[1]
                if(fragment is UnlockFragment){
                    fragment.updateList()
                }
            }catch (e:Exception){

            }
        }
    }

    override fun onStop() {
        super.onStop()
        if(RegisterActivity.closeAppAc){
            finish()
        }
    }
}