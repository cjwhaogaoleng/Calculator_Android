package com.example.counter.module.advanced

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.counter.bean.DataBean
import com.example.counter.module.advanced.AdvancedCalculatorActivity.Companion.binding
import com.example.counter.util.advancedCalculator.MixedOperation
import com.google.gson.Gson
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.math.BigDecimal
import java.text.DecimalFormat

class AdvancedViewModel(application: Application) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    var context: Activity? = null

    private var input: String = ""

    //    private var output = BigDecimal("0") //���������
    private var output = MutableLiveData<String>()

    private val eq = MixedOperation(10)

    private var _2ndFlag = true //记录当前切换到哪组按键 初始为sin类
    private var RadFlag = true //记录当前使用角度还是弧度
    private var calculateFlag = false//记录是否已经计算了结果
    private var canOpe = true

    private val df = DecimalFormat()

    @RequiresApi(Build.VERSION_CODES.P)
    @SuppressLint("SetTextI18n")
    fun click(view: View) {
        try {
            val TAG = "tag::"
            Log.d(TAG, "click: " + (view as Button).text)
            Log.d(TAG, "click: " + ((view).text == (binding.btFactorial.text )) )

            when ((view as Button).text) {
                binding.bt0.text, binding.bt1.text, binding.bt2.text,
                binding.bt3.text, binding.bt4.text, binding.bt5.text,
                binding.bt6.text, binding.bt7.text, binding.bt8.text,
                binding.bt9.text, ".", "(", ")" -> {
                    input += (view).text
                    canOpe = true
                }
                "+", "-", "×", "÷" -> {
                    if (canOpe) {
                        input += (view).text
                        canOpe = false
                    }
                }
                binding.btEqual.text -> {
//                    output = eq.getMixedOperationRes(input)
                    //获取结果
                    getResult(view, if (RadFlag) "yes" else "no")



                    calculateFlag = true
                }

                binding.btCe.text -> {
                    input = ""
                }

                binding.btBackspace.text -> {
                    input = input.substring(0, input.length - 1)
                }

                binding.btMod.text -> {
                    input += "mod"
                }

                binding.btFactorial.text ,"n!"-> {
                    input += "!"



                }

                binding.btReciprocal.text -> {
//                    output = BigDecimal.ONE.divide(
//                        BigDecimal(input),
//                        eq.bdm.accuracy,
//                        BigDecimal.ROUND_HALF_EVEN
//                    )
                    input+="^-1"
                    calculateFlag = true
                }

                "Deg" -> {
                    binding.btDegRad.text = "Rad"
                    RadFlag = false
                }
                "Rad" -> {
                    binding.btDegRad.text = "Deg"
                    RadFlag = true
                }

                binding.btSecondFunction.text -> {
                    if (_2ndFlag) {
                        binding.btSin.text = "sin"
                        binding.btCos.text = "cos"
                        binding.btTan.text = "tan"
                        binding.btSqrt.text = "2√x"
                        binding.btSquare.text = "x^2"
                        binding.btPower.text = "x^n"
                        binding.btLog.text = "log"
                        binding.btLn.text = "ln"
                        binding.btPi.text = "π"
                        _2ndFlag = false
                    } else {
                        binding.btSin.text = "asin"
                        binding.btCos.text = "acos"
                        binding.btTan.text = "atan"
                        binding.btSqrt.text = "3√x"
                        binding.btSquare.text = "x^3"
                        binding.btPower.text = "n√x"
                        binding.btLog.text = "logy(x)"
                        binding.btLn.text = "e^x"
                        binding.btPi.text = "e"
                        _2ndFlag = true
                    }
                }

                "sin" -> {
                    input +="sin"
                    calculateFlag = true
                }

                "cos" -> {

                    input+="cos"
                    calculateFlag = true
                }

                "tan" -> {

                    input+="tan"
                    calculateFlag = true
                }

                "asin" -> {

                    input+="asin"
                    calculateFlag = true
                }
                "acos" -> {
                    input += "acos"

                    calculateFlag = true
                }

                "atan" -> {
                    input += "atan"

                    calculateFlag = true
                }
                "2√x" -> {
                    input += "^1/2"

                    calculateFlag = true
                }
                "3√x" -> {

                    input+="^1/3"
                    calculateFlag = true
                }
                "x^2" -> {

                    input+="^2"
                    calculateFlag = true
                }
                "x^3" -> {

                    input+="^3"
                    calculateFlag = true
                }
                "x^n" -> {
                    input += "^"
                }
                "n√x" -> {
                    input += "√"
                }
                "log" -> {
                    input+="log"
                    calculateFlag = true
                }
                "logy(x)" -> {
                    input += "log"
                }
                "ln" -> {
input+="ln"
                    calculateFlag = true
                }
                "e^x" -> {
input+="e^"
                    calculateFlag = true
                }
                "π" -> {
input+="π"
                    calculateFlag = true
                }
                "e" -> {
input+="e"
                    calculateFlag = true
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        finally {
            binding.tvProcess.text = input

            if (calculateFlag) {
                input = output.toString()
                calculateFlag = false
            }
        }

    }
    @RequiresApi(Build.VERSION_CODES.P)
    private fun getResult(view: View, deg: String) {

        val okHttpClient = OkHttpClient.Builder()
            .build()

        val body = FormBody.Builder().add("formula", input)
            .add("deg", deg)
            .build()

        val request =
            Request.Builder().url("http://8.130.110.171:80/get_result/").post(body)
                .build()

        val call = okHttpClient?.newCall(request)

        var bean: DataBean = DataBean()
        Thread {
            val response = call?.execute()

            bean =
                Gson().fromJson<DataBean>(response!!.body()!!.string(), DataBean::class.java)

            context?.runOnUiThread {
                binding.tvResult.text = bean.result
            }
        }.start()
    }



}