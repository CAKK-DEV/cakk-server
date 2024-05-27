package com.cakk.api.dto.request.shop;

import java.time.LocalTime;
import java.util.List;

import jakarta.validation.constraints.NotNull;

import com.cakk.common.enums.Days;

public record OperationDay(

	@NotNull
	List<Days> days,
	@NotNull
	List<LocalTime> startTimes,
	@NotNull
	List<LocalTime> endTimes
) {
}
