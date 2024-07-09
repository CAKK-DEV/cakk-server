package com.cakk.api.dto.param.operation;

import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;

import com.cakk.common.enums.Days;

public record ShopOperationParam(
	@NotNull
	Days operationDay,
	@NotNull
	LocalTime operationStartTime,
	@NotNull
	LocalTime operationEndTime
) {
}
