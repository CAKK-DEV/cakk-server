package com.cakk.api.dto.request.shop;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record SearchShopByLocationRequest(

	@Min(-90) @Max(90)
	Double latitude,
	@Min(-180) @Max(180)
	Double longitude
) {
}

