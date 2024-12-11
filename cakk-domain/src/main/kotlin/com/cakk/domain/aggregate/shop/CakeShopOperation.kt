package com.cakk.domain.aggregate.shop

import java.time.LocalTime
import com.cakk.common.enums.Days
import com.cakk.domain.base.ValueObject

class CakeShopOperation(
	operationDay: Days,
	operationStartTime: LocalTime,
	operationEndTime: LocalTime,
) : ValueObject<CakeShopOperation>() {

	var operationDay: Days = operationDay
		private set

	var operationStartTime: LocalTime = operationStartTime
		private set

	var operationEndTime: LocalTime = operationEndTime
		private set
}
