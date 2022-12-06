package com.demo.safelocker.entity

import android.graphics.drawable.Drawable

class AppInfoEntity(
    var title:String = "",
    var packageName:String = "",
    var icon : Drawable? = null,
    var locked:Boolean=false,
    var index:Int=0,
)