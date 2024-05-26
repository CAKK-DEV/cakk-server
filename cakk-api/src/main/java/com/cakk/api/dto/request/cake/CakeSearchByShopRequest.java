package com.cakk.api.dto.request.cake;

import jakarta.validation.constraints.NotNull;

public record CakeSearchByShopRequest(
	Long cakeId,
	@NotNull
	Long cakeShopId,
	@NotNull
	Integer pageSize
) {
}
