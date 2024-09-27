package com.cakk.api.annotation

import jakarta.validation.Constraint
import jakarta.validation.Payload

import kotlin.reflect.KClass

import com.cakk.api.validator.OperationValidator

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
@Constraint(validatedBy = [OperationValidator::class])
annotation class OperationDay(

    val message: String = "영업 일자 형식이 잘못됐습니다.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
