package com.cakk.api.dto.request.shop;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record SearchShopByLocationRequest(

	@NotNull @Min(-90) @Max(90)
	Double latitude,
	@NotNull @Min(-180) @Max(180)
	Double longitude,
	@Min(0) @Max(10000)
	Double distance
) {
}

