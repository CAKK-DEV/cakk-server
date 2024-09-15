package com.cakk.domain.mysql.dto.param.shop

import com.cakk.common.enums.Days
import java.time.LocalTime

data class ShopOperationParam(
	val cakeShopId: Long,
	val operationDay: Days,
	val operationStartTime: LocalTime,
	val operationEndTime: LocalTime
)
