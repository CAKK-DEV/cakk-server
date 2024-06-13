package com.cakk.api.dto.request.shop;

import java.time.LocalTime;
import java.util.List;

import com.cakk.common.enums.Days;

public record OperationDays(

	List<Days> days,
	List<LocalTime> startTimes,
	List<LocalTime> endTimes
) {
}
