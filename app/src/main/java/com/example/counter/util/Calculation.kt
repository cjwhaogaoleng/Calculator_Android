package com.example.counter.util

import java.lang.IllegalArgumentException

class Calculation {
    companion object{
        fun calculate(expression: String): Double {
            val element = expression.split(" ")
            var result = element[0].toDouble()
            for (i in 1 until element.size step 2) {
                val operator = element[i]

                val number = element[i + 1].toDouble()
                when (operator) {
                    "+" -> {
                        result += number
                    }
                    "-" -> {
                        result -= number
                    }
                    "×" -> {
                        result *= number
                    }
                    "/" -> {
                        result /= number
                    }
                    else -> {
                        throw IllegalArgumentException("Invalid operator: $operator")

                    }
                }

            }
            return result
        }
    }
}