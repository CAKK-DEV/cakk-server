package com.cakk.core.dto.param.shop

import java.time.LocalTime
import com.cakk.common.enums.Days

data class ShopOperationParam(
	val operationDay: Days,
	val operationStartTime: LocalTime,
	val operationEndTime: LocalTime
)
