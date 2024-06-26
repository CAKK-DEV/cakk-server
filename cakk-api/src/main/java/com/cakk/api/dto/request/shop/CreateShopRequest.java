package com.cakk.api.dto.request.shop;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.cakk.api.annotation.OperationDay;

public record CreateShopRequest(
	@Size(max = 20)
	String businessNumber,
	@NotNull @OperationDay
	OperationDays operationDays,
	@NotBlank @Size(max = 30)
	String shopName,
	@Size(max = 40)
	String shopBio,
	@Size(max = 500)
	String shopDescription,
	@NotNull @Min(-90) @Max(90)
	Double latitude,
	@NotNull @Min(-180) @Max(180)
	Double longitude
) {
}
