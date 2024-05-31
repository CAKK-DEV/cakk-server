package com.cakk.api.dto.request.shop;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.cakk.api.validator.OperationDays;

public record CreateShopRequest(

	@NotBlank
	String businessNumber,
	@OperationDays
	OperationDayRequest operationDayRequest,
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
