package com.demo.safelocker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.safelocker.R
import com.demo.safelocker.entity.KeyboardEntity
import kotlinx.android.synthetic.main.item_keyboard.view.*

class KeyboardAdapter(
    private val context: Context,
    private val click:(key:String)->Unit
):RecyclerView.Adapter<KeyboardAdapter.KeyView>() {
    private val list= arrayListOf<KeyboardEntity>()
    init {
        list.add(KeyboardEntity(key = "1", icon = R.drawable.k1))
        list.add(KeyboardEntity(key = "2", icon = R.drawable.k2))
        list.add(KeyboardEntity(key = "3", icon = R.drawable.k3))
        list.add(KeyboardEntity(key = "4", icon = R.drawable.k4))
        list.add(KeyboardEntity(key = "5", icon = R.drawable.k5))
        list.add(KeyboardEntity(key = "6", icon = R.drawable.k6))
        list.add(KeyboardEntity(key = "7", icon = R.drawable.k7))
        list.add(KeyboardEntity(key = "8", icon = R.drawable.k8))
        list.add(KeyboardEntity(key = "9", icon = R.drawable.k9))
        list.add(KeyboardEntity(key = "c", icon = R.drawable.kc))
        list.add(KeyboardEntity(key = "0", icon = R.drawable.k0))
        list.add(KeyboardEntity(key = "d", icon = R.drawable.kd))
    }

    inner class KeyView(view:View):RecyclerView.ViewHolder(view){
        init {
            view.setOnClickListener { click.invoke(list[layoutPosition].key) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeyView {
        return KeyView(LayoutInflater.from(context).inflate(R.layout.item_keyboard,parent,false))
    }

    override fun onBindViewHolder(holder: KeyView, position: Int) {
        with(holder.itemView){
            val keyboardEntity = list[position]
            iv_keyboard.setImageResource(keyboardEntity.icon)
        }
    }

    override fun getItemCount(): Int = list.size


}