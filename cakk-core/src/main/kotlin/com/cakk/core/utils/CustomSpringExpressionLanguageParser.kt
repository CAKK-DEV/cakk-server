package com.cakk.core.utils

import org.springframework.expression.ExpressionParser
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext

object CustomSpringExpressionLanguageParser {

    fun getDynamicValue(spEL: String, parameterNames: Array<String>, args: Array<Any>): Any? {
        val parser: ExpressionParser = SpelExpressionParser()
        val context = StandardEvaluationContext()

        for (i in parameterNames.indices) {
            context.setVariable(parameterNames[i], args[i])
        }

        return parser.parseExpression(spEL).getValue(context, Any::class.java)
    }
}
