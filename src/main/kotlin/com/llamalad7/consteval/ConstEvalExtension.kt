package com.llamalad7.consteval

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid

class ConstEvalExtension : IrGenerationExtension {
    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        moduleFragment.transform(ConstEvalTransformer(pluginContext), null)
    }
}

private class ConstEvalTransformer(private val pluginContext: IrPluginContext) : IrElementTransformerVoid() {
    override fun visitCall(expression: IrCall): IrExpression {
        val result = FunctionInterpreter.evaluate(expression) ?: return super.visitCall(expression)
        return result.toIrConst(pluginContext, expression.startOffset, expression.endOffset)
    }
}