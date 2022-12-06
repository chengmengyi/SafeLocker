package com.demo.safelocker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.safelocker.R
import com.demo.safelocker.entity.AppInfoEntity
import kotlinx.android.synthetic.main.item_app.view.*

class AppAdapter(
    private val context: Context,
    private val list:ArrayList<AppInfoEntity>,
    private val click:(info:AppInfoEntity)->Unit
):RecyclerView.Adapter<AppAdapter.AppView>() {

    inner class AppView (view:View):RecyclerView.ViewHolder(view){
        init {
            view.setOnClickListener { click.invoke(list[layoutPosition]) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppAdapter.AppView {
        return AppView(
            LayoutInflater.from(context).inflate(R.layout.item_app, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AppView, position: Int) {
        with(holder.itemView){
            val appInfoEntity = list[position]
            iv_icon.setImageDrawable(appInfoEntity.icon)
            tv_title.text=appInfoEntity.title
            icon_lock.setImageResource(if (appInfoEntity.locked) R.drawable.locked else R.drawable.unlock)
        }
    }

    override fun getItemCount(): Int = list.size
}