package com.cakk.common.dto;

import java.time.LocalTime;
import java.util.List;

import com.cakk.common.enums.Days;

public record OperationDays(

	List<Days> days,
	List<LocalTime> startTimes,
	List<LocalTime> endTimes
) {
}
