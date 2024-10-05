package com.llamalad7.consteval

import org.jetbrains.kotlin.backend.jvm.ir.receiverAndArgs
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrBody
import org.jetbrains.kotlin.ir.expressions.IrBreak
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrConst
import org.jetbrains.kotlin.ir.expressions.IrConstKind
import org.jetbrains.kotlin.ir.expressions.IrContainerExpression
import org.jetbrains.kotlin.ir.expressions.IrContinue
import org.jetbrains.kotlin.ir.expressions.IrGetValue
import org.jetbrains.kotlin.ir.expressions.IrReturn
import org.jetbrains.kotlin.ir.expressions.IrSetValue
import org.jetbrains.kotlin.ir.expressions.IrStringConcatenation
import org.jetbrains.kotlin.ir.expressions.IrTypeOperator
import org.jetbrains.kotlin.ir.expressions.IrTypeOperatorCall
import org.jetbrains.kotlin.ir.expressions.IrWhen
import org.jetbrains.kotlin.ir.expressions.IrWhileLoop
import org.jetbrains.kotlin.ir.util.statements
import org.jetbrains.kotlin.ir.visitors.IrElementVisitor

class FunctionInterpreter(
    private val myFunction: IrFunction?,
) : IrElementVisitor<EvalResult, EvalData> {
    override fun visitElement(element: IrElement, data: EvalData): EvalResult {
        cannotEval()
    }

    override fun visitVariable(declaration: IrVariable, data: EvalData): EvalResult {
        data.declare(declaration.symbol, declaration.initializer?.accept(this, data))
        return EvalResult.Unit
    }

    override fun visitBody(body: IrBody, data: EvalData): EvalResult {
        return body.statements.evaluateAll(EvalData(parent = data))
    }

    override fun visitContainerExpression(expression: IrContainerExpression, data: EvalData): EvalResult {
        return expression.statements.evaluateAll(EvalData(parent = data))
    }

    override fun visitBreak(jump: IrBreak, data: EvalData): EvalResult {
        throw Signal.Break(jump.loop)
    }

    override fun visitContinue(jump: IrContinue, data: EvalData): EvalResult {
        throw Signal.Continue(jump.loop)
    }

    override fun visitCall(expression: IrCall, data: EvalData): EvalResult {
        val function = expression.symbol.owner
        if (function.isEvalFunction()) {
            return evaluate(
                expression,
                this,
                data,
            ) ?: cannotEval()
        }
        return evaluateCall(
            function,
            expression.receiverAndArgs().map {
                it.accept(this, data)
            }
        ) ?: cannotEval()
    }

    override fun visitConst(expression: IrConst<*>, data: EvalData): EvalResult {
        return when (expression.kind) {
            IrConstKind.Int -> EvalResult.IntConst(expression.value as Int)
            IrConstKind.Boolean -> EvalResult.BooleanConst(expression.value as Boolean)
            IrConstKind.String -> EvalResult.StringConst(expression.value as String)
            else -> cannotEval()
        }
    }

    override fun visitWhileLoop(loop: IrWhileLoop, data: EvalData): EvalResult {
        while (loop.condition.accept(this, data).valueAs()) {
            try {
                loop.body?.accept(this, data)
            } catch (brk: Signal.Break) {
                if (brk.loop == loop) {
                    break
                }
                throw brk
            } catch (cnt: Signal.Continue) {
                if (cnt.loop == loop) {
                    continue
                }
                throw cnt
            }
        }
        return EvalResult.Unit
    }

    override fun visitReturn(expression: IrReturn, data: EvalData): EvalResult {
        if (expression.returnTargetSymbol.owner == myFunction) {
            throw Signal.Return(expression.value.accept(this, data))
        }
        cannotEval()
    }

    override fun visitStringConcatenation(expression: IrStringConcatenation, data: EvalData): EvalResult {
        return EvalResult.of(
            expression.arguments.joinToString("") {
                it.accept(this, data).value.toString()
            }
        )
    }

    override fun visitTypeOperator(expression: IrTypeOperatorCall, data: EvalData): EvalResult {
        if (expression.operator != IrTypeOperator.IMPLICIT_COERCION_TO_UNIT) {
            cannotEval()
        }
        expression.argument.accept(this, data)
        return EvalResult.Unit
    }

    override fun visitGetValue(expression: IrGetValue, data: EvalData): EvalResult {
        return data.get(expression.symbol) ?: cannotEval()
    }

    override fun visitSetValue(expression: IrSetValue, data: EvalData): EvalResult {
        data.set(expression.symbol, expression.value.accept(this, data))
        return EvalResult.Unit
    }

    override fun visitWhen(expression: IrWhen, data: EvalData): EvalResult {
        for (branch in expression.branches) {
            val matches = branch.condition.accept(this, data).valueAs<Boolean>()
            if (matches) {
                return branch.result.accept(this, data)
            }
        }
        return EvalResult.Unit
    }

    private fun List<IrStatement>.evaluateAll(data: EvalData) =
        asSequence().map { it.accept(this@FunctionInterpreter, data) }.lastOrNull() ?: EvalResult.Unit

    private inline fun <reified T> EvalResult.valueAs() = value as? T ?: cannotEval()

    private fun cannotEval(): Nothing = throw Signal.CannotEval

    companion object {
        fun evaluate(
            call: IrCall,
            argInterpreter: FunctionInterpreter = FunctionInterpreter(null),
            argContext: EvalData = EvalData(),
        ): EvalResult? {
            val function = call.symbol.owner
            if (!function.isEvalFunction()) {
                return null
            }
            val interpreter = FunctionInterpreter(function)
            val startingData = EvalData.fromArguments(
                call,
                interpreter,
                argInterpreter,
                argContext,
            ) ?: return null
            try {
                function.body?.accept(interpreter, startingData)
            } catch (ret: Signal.Return) {
                return ret.returnValue
            } catch (_: Signal.CannotEval) {
                return null
            }
            error("Call to ${function.name} didn't return a value!")
        }
    }
}