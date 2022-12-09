package com.demo.safelocker.entity

open class AdmobEntity(
    val slk_tt:String,
    val slk_ww:Int,
    val slk_ss:String,
    val slk_ii:String,
){
    override fun toString(): String {
        return "AdmobEntity(slk_tt='$slk_tt', slk_ww=$slk_ww, slk_ss='$slk_ss', slk_ii='$slk_ii')"
    }
}