package com.demo.safelocker.dialog

import android.content.Intent
import com.demo.safelocker.R
import com.demo.safelocker.ac.lock.AppAc
import com.demo.safelocker.base.BaseDialog
import kotlinx.android.synthetic.main.dialog_set_pwd_success.*

class SetPwdSuccessDialog():BaseDialog() {

    override fun layoutId(): Int = R.layout.dialog_set_pwd_success

    override fun onView() {
        dialog?.setCancelable(false)
        tv_sure.setOnClickListener {
            requireContext().startActivity(Intent(requireContext(),AppAc::class.java))
            dismiss()
        }
    }

}