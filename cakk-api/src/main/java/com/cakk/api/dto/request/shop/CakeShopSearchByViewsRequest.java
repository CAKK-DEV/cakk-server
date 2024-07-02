package com.cakk.api.dto.request.shop;

import jakarta.validation.constraints.NotNull;

public record CakeShopSearchByViewsRequest(
	Long offset,
	@NotNull
	Integer pageSize
) {
}
