package com.cakk.admin.validator

import java.util.*

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

import com.cakk.admin.annotation.OperationDay
import com.cakk.common.enums.Days
import com.cakk.infrastructure.persistence.param.shop.ShopOperationParam

class OperationValidator : ConstraintValidator<OperationDay, List<com.cakk.infrastructure.persistence.param.shop.ShopOperationParam>> {

	override fun isValid(operationParams: List<com.cakk.infrastructure.persistence.param.shop.ShopOperationParam>?, context: ConstraintValidatorContext): Boolean {
		operationParams ?: return false

		val days: MutableMap<Days, Boolean> = EnumMap(Days::class.java)
		for (operationParam in operationParams) {
			if (days.containsKey(operationParam.operationDay)) {
				return false
			} else {
				days[operationParam.operationDay] = true
			}
		}
		return true
	}
}
