package com.demo.safelocker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.safelocker.R
import kotlinx.android.synthetic.main.item_pwd.view.*

class PwdAdapter(private val context: Context):RecyclerView.Adapter<PwdAdapter.PwdView>() {
    private var showFail=false
    private var pwdSize=0

    fun updatePwdSize(list:ArrayList<String>,fail:Boolean){
        this.pwdSize=list.size
        this.showFail=fail
        notifyDataSetChanged()
    }

    inner class PwdView(view:View):RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PwdView {
        return PwdView(LayoutInflater.from(context).inflate(R.layout.item_pwd,parent,false))
    }

    override fun onBindViewHolder(holder: PwdView, position: Int) {
        with(holder.itemView){
            if (showFail){
                iv_pwd.setImageResource(R.drawable.pwd_fail)
            }else{
                iv_pwd.setImageResource(if (pwdSize>=(position+1)) R.drawable.pwd_selected else R.drawable.pwd_unselect)
            }
        }
    }

    override fun getItemCount(): Int = 4
}