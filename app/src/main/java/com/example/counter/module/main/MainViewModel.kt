package com.example.counter.module.main

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.view.View
import android.widget.Button
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.lifecycle.AndroidViewModel
import com.example.counter.R
import com.example.counter.module.main.MainActivity.Companion.binding
import com.example.counter.util.Calculation
import java.lang.Exception

class MainViewModel(application: Application) : AndroidViewModel(application) {
    @SuppressLint("StaticFieldLeak")
    var context: Activity? = null

    //小数点判断
    private var canDot = true

    private var useOperational = false

    var str = ""

    fun click2(v: View) {
        when (v.id) {

            R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4, R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9 -> {
                str += (v as Button).text
                useOperational = false
                binding.tvInput.setText(str)
            }

            R.id.btn_pt -> {
                if (canDot && !useOperational) {
                    str += "."
                    canDot = false
                    binding.tvInput.setText(str)
                }
            }

            R.id.btn_add, R.id.btn_sub, R.id.btn_mul, R.id.btn_div -> {
                // 判断最近有没有用过运算符
                if (str.length >= 3 && (str[str.length - 2] == '+' || str[str.length - 2] == '-' ||
                            str[str.length - 2] == '×' || str[str.length - 2] == '/')
                ) {
                    str = str.substring(0, str.length - 3)
                    useOperational = true
                }
                if (str != "" && str.last() == '.')
                    str = str.substring(0, str.length - 1)
                str += " ${(v as Button).text} "
                canDot = true
                binding.tvInput.setText(str)
            }
            R.id.btn_clr -> {

                useOperational = false
                canDot = true
                str = ""
                binding.tvInput.setText("")
            }
            R.id.btn_del -> {
                if (str.isEmpty()) {
                    return
                } else {
                    if (!canDot) {
                        canDot = true
                    }
                    if(str[str.length - 1]==' '){
                        str = str.removeRange(str.length - 3, str.length - 1)
                    }
                    if (useOperational) {
                        useOperational = false
                    } else {
                        str = str.substring(0, str.length - 1)
                    }
                    binding.tvInput.setText(str)
                }
            }
            R.id.btn_eq -> {
                //如果最后是个操作符
                if (useOperational) {
                    str = str.substring(0, str.length - 3)
                    useOperational = false
                }
//                如果最后是个小数点
                if (str != "" && str.last() == '.')
                    str = str.substring(0, str.length - 1)
                try {
                    var result = Calculation.calculate(str)
                    if (result % 1 == 0.0) {
                        str = (result.toInt()).toString()
                    }
                    str = result.toString()
                    binding.tvInput.setText(str)
                    canDot = true
                    useOperational = false
                } catch (e: Exception) {
                    makeText(context, "\"运算失败，输入有误\"", Toast.LENGTH_SHORT).show()
                    makeText(context, e.printStackTrace().toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            } //调用下面的方法
        }
    }
}