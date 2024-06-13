package com.cakk.api.dto.request.shop;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.cakk.api.annotation.OperationDay;

public record CreateShopRequest(

	@NotBlank
	String businessNumber,
	@OperationDay
	OperationDays operationDays,
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
