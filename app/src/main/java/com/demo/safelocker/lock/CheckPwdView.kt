package com.demo.safelocker.lock

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.safelocker.R
import com.demo.safelocker.adapter.KeyboardAdapter
import com.demo.safelocker.adapter.PwdAdapter

@SuppressLint("StaticFieldLeak")
object CheckPwdView {
    private var showing=false
    private var view: View?=null
    private val pwd= arrayListOf<String>()
    private var tvTitle: AppCompatTextView?=null
    private var tvErrorText: AppCompatTextView?=null
    private lateinit var windowManager: WindowManager
    private lateinit var layoutParams: WindowManager.LayoutParams
    private lateinit var pwdAdapter: PwdAdapter
    private lateinit var keyboardAdapter:KeyboardAdapter


    fun createView(context: Context){
        if(null==view){
            initView(context)
            setAdapter(context)
        }
    }

    private fun initView(context: Context){
        windowManager = context.getSystemService(Service.WINDOW_SERVICE) as WindowManager
        layoutParams = WindowManager.LayoutParams()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                layoutParams.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            }
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE
        }
        layoutParams.format = PixelFormat.RGBA_8888
        layoutParams.gravity = Gravity.LEFT or Gravity.TOP
        layoutParams.flags = (WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                or WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                or WindowManager.LayoutParams.FLAG_FULLSCREEN
                or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                )
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT


        height(context)
        view = LayoutInflater.from(context).inflate(R.layout.view_check_pwd, null)
        tvTitle=view?.findViewById(R.id.tv_title)
        tvErrorText=view?.findViewById(R.id.tv_error)
    }

    private fun setAdapter(context: Context){
        val rvPwd = view?.findViewById<RecyclerView>(R.id.rv_pwd)
        val rvKeyboard = view?.findViewById<RecyclerView>(R.id.rv_keyboard)
        pwdAdapter= PwdAdapter(context)
        keyboardAdapter= KeyboardAdapter(context){
            clickKeyboard(it)
        }
        rvPwd?.apply {
            layoutManager= LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
            adapter= pwdAdapter
        }
        rvKeyboard?.apply {
            layoutManager= GridLayoutManager(context,3)
            adapter=keyboardAdapter
        }
    }

    private fun clickKeyboard(key:String){
        when(key){
            "c"->{
                pwd.clear()
                tvTitle?.isSelected=false
                setPwdErrorText("")
                pwdAdapter.updatePwdSize(pwd,false)
            }
            "d"->{
                if(pwd.isNotEmpty()){
                    pwd.removeLast()
                    tvTitle?.isSelected=false
                    setPwdErrorText("")
                    pwdAdapter.updatePwdSize(pwd,false)
                }
            }
            else->{
                if(pwd.size<4){
                    pwd.add(key)
                    pwdAdapter.updatePwdSize(pwd,false)
                    if(pwd.size==4){
                        enterPwdCompleted()
                    }
                }
            }
        }
    }

    private fun enterPwdCompleted(){
        if(LockPwdManager.checkPwd(pwd())){
            pwd.clear()
            pwdAdapter.updatePwdSize(pwd,false)
            hideOverlay()
        }else{
            pwdAdapter.updatePwdSize(pwd,false)
            setPwdErrorText("password error")
        }
    }

    fun showOverlay(){
        if (!showing){
            showing=true
            pwd.clear()
            tvErrorText?.isSelected=false
            pwdAdapter.updatePwdSize(pwd,false)
            setPwdErrorText("")
            windowManager.addView(view, layoutParams)
        }
    }

    fun hideOverlay(){
        if (showing){
            showing=false
            windowManager.removeView(view)
        }
    }

    private fun pwd():String{
        val stringBuffer = StringBuffer()
        pwd.forEach {
            stringBuffer.append(it)
        }
        return stringBuffer.toString()
    }

    private fun setPwdErrorText(text:String){
        tvErrorText?.text=text
    }

    private fun height(context: Context) {
        val metrics: DisplayMetrics = context.resources.displayMetrics
        val td = metrics.heightPixels / 760f
        val dpi = (160 * td).toInt()
        metrics.density = td
        metrics.scaledDensity = td
        metrics.densityDpi = dpi
    }
}