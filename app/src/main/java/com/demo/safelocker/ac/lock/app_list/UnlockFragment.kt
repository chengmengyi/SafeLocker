package com.demo.safelocker.ac.lock.app_list

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.safelocker.R
import com.demo.safelocker.adapter.AppAdapter
import com.demo.safelocker.app.RegisterActivity
import com.demo.safelocker.app.hasOverPermission
import com.demo.safelocker.dialog.NoticeDialog
import com.demo.safelocker.entity.AppInfoEntity
import com.demo.safelocker.interfaces.IUpdateAppListCallback
import com.demo.safelocker.lock.AppListManager
import kotlinx.android.synthetic.main.fragment_unlock.*

class UnlockFragment:Fragment() {
    private val tips="To use the application locking function, you need to obtain the permission of \"Floating Window\""


    private var iUpdateAppListCallback:IUpdateAppListCallback?=null
    private val appAdapter by lazy { AppAdapter(requireContext(),AppListManager.unlockList){ click(it) } }

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
        return inflater.inflate(R.layout.fragment_unlock,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_app.apply {
            layoutManager=LinearLayoutManager(requireContext())
            adapter=appAdapter
        }
    }

    private fun click(appInfoEntity: AppInfoEntity){
        if(!hasOverPermission(requireContext())){
            RegisterActivity.closeAppAc=false
            NoticeDialog(tips){
                val intent= Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:${requireContext().packageName}"))
                startActivityForResult(intent, 101)
            }.show(childFragmentManager,"OverlayPermission")
            return
        }
        AppListManager.lockOrUnlock(appInfoEntity)
        appAdapter.notifyDataSetChanged()
        iUpdateAppListCallback?.updateAppList(true)
    }

    fun updateList(){
        appAdapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        RegisterActivity.closeAppAc=true
    }
}