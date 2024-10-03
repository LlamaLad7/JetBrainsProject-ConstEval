package com.llamalad7.consteval

import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.expressions.IrLoop

sealed class Signal : Throwable() {
    final override fun fillInStackTrace() = this

    data class CannotEval(val element: IrElement) : Signal()
    data class Return(val returnValue: EvalResult) : Signal()
    data class Break(val loop: IrLoop) : Signal()
    data class Continue(val loop: IrLoop) : Signal()
}