package com.llamalad7.consteval

import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrFunctionAccessExpression
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.isBoolean
import org.jetbrains.kotlin.ir.types.isInt
import org.jetbrains.kotlin.ir.types.isString

fun IrType.isConstant() = isInt() || isBoolean() || isString()

fun IrFunction.isEvalFunction() =
    name.asString().startsWith("eval")
            && returnType.isConstant()
            && valueParameters.all { it.type.isConstant() }

val IrFunctionAccessExpression.arguments: List<IrExpression?>
    get() = List(valueArgumentsCount) { getValueArgument(it) }

fun IrFunction.propertyOrFunctionName() = (this as? IrSimpleFunction)?.correspondingPropertySymbol?.owner?.name ?: name