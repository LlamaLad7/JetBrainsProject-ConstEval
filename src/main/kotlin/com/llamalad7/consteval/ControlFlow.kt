package com.llamalad7.consteval

import org.jetbrains.kotlin.ir.expressions.IrLoop

sealed class Signal : Throwable() {
    final override fun fillInStackTrace() = this

    data object CannotEval : Signal()
    data class Return(val returnValue: EvalResult) : Signal()
    data class Break(val loop: IrLoop) : Signal()
    data class Continue(val loop: IrLoop) : Signal()
}