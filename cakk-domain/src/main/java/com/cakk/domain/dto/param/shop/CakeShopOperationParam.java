package com.cakk.domain.dto.param.shop;

import java.time.LocalTime;

import com.cakk.common.enums.Days;

public record CakeShopOperationParam(
	Days operationDay,
	LocalTime operationStartTime,
	LocalTime operationEndTime
) {
}
