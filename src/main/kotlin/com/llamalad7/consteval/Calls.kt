package com.llamalad7.consteval

import org.jetbrains.kotlin.ir.BuiltInOperatorNames
import org.jetbrains.kotlin.ir.declarations.IrFunction

fun evaluateCall(function: IrFunction, args: List<EvalResult>): EvalResult? {
    val realArgs = args.map { it.value }
    val name = function.propertyOrFunctionName().asString()
    return when (args.size) {
        1 -> evaluateUnary(name, realArgs[0])
        2 -> evaluateBinary(name, realArgs[0], realArgs[1])
        else -> null
    }?.let { EvalResult.of(it) }
}

private fun evaluateUnary(name: String, value: Any): Any? {
    when (value) {
        is Int -> when (name) {
            "inc" -> return value.inc()
            "dec" -> return value.dec()
            "unaryPlus" -> return value.unaryPlus()
            "unaryMinus" -> return value.unaryMinus()
            "inv" -> return value.inv()
            "toInt" -> return value.toInt()
        }

        is Boolean -> when (name) {
            "not" -> return value.not()
        }

        is String -> when (name) {
            "length" -> return value.length
            "trimIndent" -> return value.trimIndent()
        }
    }
    when (name) {
        "hashCode" -> return value.hashCode()
        "toString" -> return value.toString()
    }
    return null
}

private fun evaluateBinary(name: String, left: Any, right: Any): Any? {
    when (left) {
        is Int -> when (right) {
            is Int -> when (name) {
                "compareTo" -> return left.compareTo(right)
                "plus" -> return left.plus(right)
                "minus" -> return left.minus(right)
                "times" -> return left.times(right)
                "div" -> return left.div(right)
                "rem" -> return left.rem(right)
                "shl" -> return left.shl(right)
                "shr" -> return left.shr(right)
                "ushr" -> return left.ushr(right)
                "and" -> return left.and(right)
                "or" -> return left.or(right)
                "xor" -> return left.xor(right)
                BuiltInOperatorNames.LESS -> return left < right
                BuiltInOperatorNames.LESS_OR_EQUAL -> return left <= right
                BuiltInOperatorNames.GREATER -> return left > right
                BuiltInOperatorNames.GREATER_OR_EQUAL -> return left >= right
            }
        }

        is Boolean -> when (right) {
            is Boolean -> when (name) {
                "and" -> return left.and(right)
                "or" -> return left.or(right)
                "xor" -> return left.xor(right)
                "compareTo" -> return left.compareTo(right)
            }
        }

        is String -> {
            when (right) {
                is String -> when (name) {
                    "compareTo" -> return left.compareTo(right)
                }
            }
            when (name) {
                "plus" -> return left.plus(right)
            }
        }
    }
    when (name) {
        "equals", BuiltInOperatorNames.EQEQ -> return left == right
        BuiltInOperatorNames.EQEQEQ -> return left === right
    }
    return null
}