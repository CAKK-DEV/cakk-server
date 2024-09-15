package com.cakk.domain.mysql.dto.param.shop

import com.cakk.common.enums.Days
import java.time.LocalTime

data class CakeShopOperationParam(
	val operationDay: Days,
	val operationStartTime: LocalTime,
	val operationEndTime: LocalTime
)
