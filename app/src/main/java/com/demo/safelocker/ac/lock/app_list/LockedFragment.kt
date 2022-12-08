package com.demo.safelocker.ac.lock.app_list

import android.content.Context
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ImageSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.safelocker.R
import com.demo.safelocker.adapter.AppAdapter
import com.demo.safelocker.admob.AdType
import com.demo.safelocker.admob.ShowLockAdmob
import com.demo.safelocker.app.show
import com.demo.safelocker.entity.AppInfoEntity
import com.demo.safelocker.interfaces.IUpdateAppListCallback
import com.demo.safelocker.lock.AppListManager
import com.demo.safelocker.lock.LockPwdManager
import kotlinx.android.synthetic.main.fragment_locked.*

class
LockedFragment:Fragment() {
    private var iUpdateAppListCallback:IUpdateAppListCallback?=null
    private val showFullAdmob by lazy { ShowLockAdmob(requireActivity(), AdType.LOCK) }
    private val appAdapter by lazy { AppAdapter(requireContext(),AppListManager.lockedList){ click(it) } }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            iUpdateAppListCallback=context as IUpdateAppListCallback
        }catch (e:Exception){

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_locked,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTips()
        rv_app.apply {
            layoutManager=LinearLayoutManager(requireContext())
            adapter=appAdapter
        }
    }

    private fun click(appInfoEntity: AppInfoEntity){
        AppListManager.lockOrUnlock(appInfoEntity)
        appAdapter.notifyDataSetChanged()
        iUpdateAppListCallback?.updateAppList(false)
        setTips()
        LockPwdManager.lockUnlockNum++
        if(LockPwdManager.showFullAd()){
            showFullAdmob.showFullAd()
        }
    }

    private fun setTips(){
        if(AppListManager.lockedList.isNotEmpty()){
            tv_tips.show(false)
            return
        }
        tv_tips.show(true)
        var str="Enter Unlocked Apps list, and click % of apps to lock it now!"
        val indexOf = str.indexOf("% ")
        val spannableString= SpannableString(str)
        val d = ContextCompat.getDrawable(requireContext(),R.drawable.unlock)!!
        d.setBounds(0, 0, d.minimumWidth, d.minimumHeight)
        spannableString.setSpan(
            ImageSpan(d),
            indexOf,
            indexOf+1,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        tv_tips.text=spannableString
        tv_tips.movementMethod = LinkMovementMethod.getInstance()
    }

    fun updateList(){
        appAdapter.notifyDataSetChanged()
        setTips()
    }
}