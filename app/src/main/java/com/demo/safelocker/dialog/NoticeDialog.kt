package com.demo.safelocker.dialog

import com.demo.safelocker.R
import com.demo.safelocker.base.BaseDialog
import kotlinx.android.synthetic.main.dialog_notice.*

class NoticeDialog(private val tips:String,private val sure:()->Unit):BaseDialog() {

    override fun layoutId(): Int = R.layout.dialog_notice

    override fun onView() {
        tv_text.text=tips
        iv_cancel.setOnClickListener { dismiss() }
        tv_sure.setOnClickListener {
            sure.invoke()
            dismiss()
        }
    }

}