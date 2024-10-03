package com.llamalad7.consteval

import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.symbols.IrValueSymbol

class EvalData(private val parent: EvalData? = null) {
    private val values by lazy { mutableMapOf<IrValueSymbol, EvalResult?>() }

    fun get(variable: IrValueSymbol): EvalResult? =
        values[variable] ?: parent?.get(variable)

    fun set(variable: IrValueSymbol, value: EvalResult?) {
        if (variable in values) {
            values[variable] = value
        } else if (parent != null) {
            parent.set(variable, value)
        } else {
            throw IllegalArgumentException("Tried to set nonexistent variable: $variable")
        }
    }

    fun declare(variable: IrValueSymbol, value: EvalResult?) {
        require(variable !in values) { "Redeclared variable: $variable" }
        values[variable] = value
    }

    companion object {
        fun fromArguments(call: IrCall, defaultArgInterpreter: FunctionInterpreter): EvalData? {
            val function = call.symbol.owner
            val argInterpreter = FunctionInterpreter(null)
            val paramData = EvalData()
            try {
                for ((arg, param) in call.arguments.zip(function.valueParameters)) {
                    val result = arg?.accept(argInterpreter, EvalData())
                        ?: param.defaultValue?.accept(defaultArgInterpreter, paramData)
                        ?: return null
                    paramData.declare(param.symbol, result)
                }
            } catch (_: Signal.CannotEval) {
                return null
            }
            return paramData
        }
    }
}