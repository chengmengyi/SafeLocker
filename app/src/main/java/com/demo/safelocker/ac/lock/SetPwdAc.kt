package com.demo.safelocker.ac.lock

import android.content.Intent
import android.graphics.Paint
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.safelocker.R
import com.demo.safelocker.adapter.KeyboardAdapter
import com.demo.safelocker.adapter.PwdAdapter
import com.demo.safelocker.app.show
import com.demo.safelocker.base.BaseAc
import com.demo.safelocker.dialog.NoticeDialog
import com.demo.safelocker.dialog.SetPwdSuccessDialog
import com.demo.safelocker.lock.LockPwdManager
import com.demo.safelocker.lock.SetPwdEnum
import kotlinx.android.synthetic.main.activity_set_pwd.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class SetPwdAc:BaseAc() {
    lateinit var type:SetPwdEnum
    private val pwdList= arrayListOf<String>()
    private val pwdAdapter by lazy { PwdAdapter(this) }
    private val keyboardAdapter by lazy { KeyboardAdapter(this){ clickKeyboard(it) } }

    private val resetTips="After resetting the password, all contents will be reset. Confirm to reset the password"

    override fun layoutId(): Int = R.layout.activity_set_pwd

    override fun view() {
        immersionBar.statusBarView(top).init()
        getSetType()
        setAdapter()
        showResetPwdText()
        iv_back.setOnClickListener { finish() }
        EventBus.getDefault().register(this)
    }

    private fun clickKeyboard(key:String){
        when(key){
            "c"->{
                pwdList.clear()
                tv_title.isSelected=false
                setPwdErrorText("")
                pwdAdapter.updatePwdSize(pwdList,false)
            }
            "d"->{
                if(pwdList.isNotEmpty()){
                    pwdList.removeLast()
                    tv_title.isSelected=false
                    setPwdErrorText("")
                    pwdAdapter.updatePwdSize(pwdList,false)
                }
            }
            else->{
                if(pwdList.size<4){
                    pwdList.add(key)
                    pwdAdapter.updatePwdSize(pwdList,false)
                    if(pwdList.size==4){
                        enterPwdCompleted()
                    }
                }
            }
        }
    }

    private fun enterPwdCompleted(){
        when(type){
            SetPwdEnum.SET_PWD->{
                startActivity(Intent(this,SetPwdAc::class.java)
                    .putExtra("type",SetPwdEnum.SET_PWD_AGAIN)
                    .putExtra("last_pwd",pwd())
                )
            }
            SetPwdEnum.SET_PWD_AGAIN->{
                val lastPwd = intent.getStringExtra("last_pwd")
                val currentPwd = pwd()
                if(lastPwd==currentPwd){
                    LockPwdManager.writePwdToLocal(currentPwd)
                    SetPwdSuccessDialog().show(supportFragmentManager,"SetPwdSuccessDialog")
                }else{
                    tv_title.isSelected=true
                    setPwdErrorText("The two passwords are inconsistent")
                    pwdAdapter.updatePwdSize(pwdList,true)
                }
            }
            SetPwdEnum.CHECK_PWD->{
                if(LockPwdManager.checkPwd(pwd())){
                    LockPwdManager.failNum=3
                    startActivity(Intent(this,AppAc::class.java))
                }else{
                    LockPwdManager.countFailNum()
                    setPwdErrorText("You can also enter ${LockPwdManager.failNum} times")
                    if(LockPwdManager.failNum<=0){
                        showResetPwdDialog()
                    }
                }
            }
        }
    }

    private fun showResetPwdDialog(){
        NoticeDialog(resetTips){
            LockPwdManager.reset()
            startActivity(Intent(this,SetPwdAc::class.java).apply {
                putExtra("type",SetPwdEnum.SET_PWD)
            })
            finish()
        }.show(supportFragmentManager,"NoticeDialog")
    }

    private fun pwd():String{
        val stringBuffer = StringBuffer()
        pwdList.forEach {
            stringBuffer.append(it)
        }
        return stringBuffer.toString()
    }

    private fun setAdapter(){
        rv_keyboard.apply {
            layoutManager=GridLayoutManager(this@SetPwdAc,3)
            adapter=keyboardAdapter
        }
        rv_pwd.apply {
            layoutManager=LinearLayoutManager(this@SetPwdAc,LinearLayoutManager.HORIZONTAL,false)
            adapter=pwdAdapter
        }
    }

    private fun getSetType(){
        type=intent.getSerializableExtra("type") as SetPwdEnum
        setInfoByType()
    }

    private fun setInfoByType(){
        tv_title.text=when(type){
            SetPwdEnum.SET_PWD->"Set Your Password"
            SetPwdEnum.SET_PWD_AGAIN->"Enter Your Password Again"
            SetPwdEnum.CHECK_PWD->"Enter Your Password"
        }
    }

    private fun setPwdErrorText(text:String){
        tv_error.text=text
    }

    private fun showResetPwdText(){
        if(type==SetPwdEnum.CHECK_PWD){
            tv_reset_pwd.show(true)
            tv_reset_pwd.paint.flags = Paint.UNDERLINE_TEXT_FLAG
            tv_reset_pwd.paint.isAntiAlias = true
            tv_reset_pwd.setOnClickListener { showResetPwdDialog() }
        }
    }

    @Subscribe
    fun onEvent(string: String) {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}