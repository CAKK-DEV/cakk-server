package com.cakk.api.dto.request.shop;

import java.time.LocalTime;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.cakk.common.enums.DayOfWeek;

public record CreateShopRequest(

	@NotBlank
	String businessNumber,
	@NotNull
	@Size(min = 1, max = 7)
	List<DayOfWeek> operationsDays,
	@NotNull
	@Size(min = 1, max = 7)
	List<LocalTime> startTimes,
	@NotNull
	@Size(min = 1, max = 7)
	List<LocalTime> endTimes,
	@NotBlank
	String shopName,
	String shopBio,
	String shopDescription,
	@NotNull
	Double latitude,
	@NotNull
	Double longitude
) {
}
