package com.cakk.api.validator

import java.util.*

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

import com.cakk.api.annotation.OperationDay
import com.cakk.common.enums.Days
import com.cakk.core.dto.param.shop.ShopOperationParam

class OperationValidator : ConstraintValidator<OperationDay, List<ShopOperationParam>> {
	override fun isValid(operationParams: List<ShopOperationParam>?, context: ConstraintValidatorContext): Boolean {
		operationParams ?: return false

		val days: MutableMap<Days, Boolean> = EnumMap(Days::class.java)
		operationParams.forEach {
			when {
				days.containsKey(it.operationDay) -> return false
				else -> days[it.operationDay] = true
			}
		}

		return true
	}
}
