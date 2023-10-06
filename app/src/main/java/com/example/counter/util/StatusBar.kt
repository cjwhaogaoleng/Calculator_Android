package com.example.counter.util

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.WindowManager

//修改状态栏的工具
class StatusBar     //初始化activity
    (private val activity: Activity) {
    //将状态栏设置为传入的color
    fun setColor(color: Int) {
        if (Build.VERSION.SDK_INT >= 21) {
            val view = activity.window.decorView
            view.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            activity.window.statusBarColor = activity.resources.getColor(color)
        }
    }

    //隐藏状态栏
    fun hide() {
        if (Build.VERSION.SDK_INT >= 21) {
            activity.window
                .setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
                )
        }
    }

    //设置状态栏字体颜色
    fun setTextColor(isDarkBackground: Boolean) {
        val decor = activity.window.decorView
        if (isDarkBackground) {
            //黑暗背景字体浅色
            decor.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        } else {
            //高亮背景字体深色
            decor.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }
}
