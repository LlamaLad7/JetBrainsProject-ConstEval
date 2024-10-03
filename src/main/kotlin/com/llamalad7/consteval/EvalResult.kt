package com.llamalad7.consteval

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.expressions.IrConst
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.util.toIrConst

sealed class EvalResult {
    data class IntConst(override val value: Int) : EvalResult() {
        override fun getIrType(context: IrPluginContext) = context.irBuiltIns.intType
    }

    data class BooleanConst(override val value: Boolean) : EvalResult() {
        override fun getIrType(context: IrPluginContext) = context.irBuiltIns.booleanType
    }

    data class StringConst(override val value: String) : EvalResult() {
        override fun getIrType(context: IrPluginContext) = context.irBuiltIns.stringType
    }

    abstract val value: Any

    abstract fun getIrType(context: IrPluginContext): IrType

    fun toIrConst(context: IrPluginContext, startOffset: Int, endOffset: Int): IrConst<*> {
        return value.toIrConst(getIrType(context), startOffset, endOffset)
    }

    companion object {
        fun of(value: Any) = when (value) {
            is Int -> IntConst(value)
            is Boolean -> BooleanConst(value)
            is String -> StringConst(value)
            else -> throw IllegalArgumentException("Unsupported eval result $value")
        }
    }
}

inline fun <reified T> EvalResult?.valueAs() =
    this?.value as? T ?: error("Cannot interpret $this as ${T::class.simpleName}")