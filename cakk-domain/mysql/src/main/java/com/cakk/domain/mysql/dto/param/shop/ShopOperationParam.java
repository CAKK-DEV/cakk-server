package com.cakk.domain.mysql.dto.param.shop;

import java.time.LocalTime;

import com.cakk.common.enums.Days;

public record ShopOperationParam(
	Long cakeShopId,
	Days operationDay,
	LocalTime operationStartTime,
	LocalTime operationEndTime
) {
}
