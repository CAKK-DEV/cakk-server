package com.cakk.api.dto.request.shop;

import java.time.LocalTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateShopRequest(

	@NotBlank
	String businessNumber,
	@NotBlank
	String operationDay,
	@NotNull
	LocalTime startTime,
	@NotNull
	LocalTime endTime,
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
