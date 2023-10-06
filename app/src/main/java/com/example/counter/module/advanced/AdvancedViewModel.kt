package com.example.counter.module.advanced

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.lifecycle.AndroidViewModel
import com.example.counter.module.advanced.AdvancedCalculatorActivity.Companion.binding
import com.example.counter.util.advancedCalculator.MixedOperation
import java.math.BigDecimal
import java.text.DecimalFormat

class AdvancedViewModel(application: Application) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    var context: Activity? = null

    private var input: String = ""
    private var output = BigDecimal("0") //���������

    private val eq = MixedOperation(10)

    private var _2ndFlag = true //记录当前切换到哪组按键 初始为sin类
    private var RadFlag = true //记录当前使用角度还是弧度
    private var calculateFlag = false//记录是否已经计算了结果
    private var canOpe = true

    private val df = DecimalFormat()

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
                "+", "-", "*", "/" -> {
                    if (canOpe) {
                        input += (view).text
                        canOpe = false
                    }
                }
                binding.btEqual.text -> {
                    output = eq.getMixedOperationRes(input)
                    calculateFlag = true
                }

                binding.btCe.text -> {
                    input = ""
                }

                binding.btBackspace.text -> {
                    input = input.substring(0, input.length - 1)
                }

                binding.btMod.text -> {
                    input += "%"
                }

                binding.btFactorial.text ,"n!"-> {
                    Log.d(TAG, "click: "+"dao")
                    output = eq.bdm.fac(input.toInt())
                    Log.d(TAG, "click: $output")

                }

                binding.btReciprocal.text -> {
                    output = BigDecimal.ONE.divide(
                        BigDecimal(input),
                        eq.bdm.accuracy,
                        BigDecimal.ROUND_HALF_EVEN
                    )
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
                        binding.btLog.text = "ylogx"
                        binding.btLn.text = "e^x"
                        binding.btPi.text = "e"
                        _2ndFlag = true
                    }
                }

                "sin" -> {
                    output = eq.bdm.sin(
                        if (RadFlag) eq.bdm.toRadians(BigDecimal(input)) else BigDecimal(input)
                    )
                    calculateFlag = true
                }

                "cos" -> {
                    output = eq.bdm.cos(
                        if (RadFlag) eq.bdm.toRadians(BigDecimal(input)) else BigDecimal(input)
                    )
                    calculateFlag = true
                }

                "tan" -> {
                    output = eq.bdm.tan(
                        if (RadFlag) eq.bdm.toRadians(BigDecimal(input)) else BigDecimal(input)
                    )
                    calculateFlag = true
                }

                "asin" -> {
                    output =
                        if (RadFlag) eq.bdm.toDegrees(eq.bdm.asin(BigDecimal(input))) else eq.bdm.asin(
                            BigDecimal(input)
                        )
                    calculateFlag = true
                }
                "acos" -> {
                    output =
                        if (RadFlag) eq.bdm.toDegrees(eq.bdm.acos(BigDecimal(input))) else eq.bdm.acos(
                            BigDecimal(input)
                        )
                    calculateFlag = true
                }

                "atan" -> {
                    output =
                        if (RadFlag) eq.bdm.toDegrees(eq.bdm.atan(BigDecimal(input))) else eq.bdm.atan(
                            BigDecimal(input)
                        )
                    calculateFlag = true
                }
                "2√x" -> {
                    output = eq.bdm.pow(BigDecimal(input), BigDecimal("0.5"))
                    calculateFlag = true
                }
                "3√x" -> {
                    output = eq.bdm.pow(
                        BigDecimal(input),
                        BigDecimal.ONE.divide(
                            BigDecimal("3"),
                            eq.bdm.accuracy,
                            BigDecimal.ROUND_HALF_EVEN
                        )
                    )
                    calculateFlag = true
                }
                "x^2" -> {
                    output = eq.bdm.pow(BigDecimal(input), BigDecimal("2"))
                    calculateFlag = true
                }
                "x^3" -> {
                    output = eq.bdm.pow(BigDecimal(input), BigDecimal("3"))
                    calculateFlag = true
                }
                "x^n" -> {
                    input += "^"
                }
                "n√x" -> {
                    input += "√"
                }
                "log" -> {
                    output = eq.bdm.log(BigDecimal.TEN, BigDecimal(input))
                    calculateFlag = true
                }
                "ylogx" -> {
                    input += "log"
                }
                "ln" -> {
                    output = eq.bdm.log(BigDecimal(input))
                    calculateFlag = true
                }
                "e^x" -> {
                    output = eq.bdm.pow(eq.bdm.e, BigDecimal(input))
                    calculateFlag = true
                }
                "π" -> {
                    output = eq.bdm.pi
                    calculateFlag = true
                }
                "e" -> {
                    output = eq.bdm.e
                    calculateFlag = true
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        finally {
            binding.tvProcess.text = input
            binding.tvResult.text = df.format(output).toString()

            if (calculateFlag) {
                input = output.toString()
                calculateFlag = false
            }
        }

    }


}