package com.cakk.api.dto.request.cake;

import jakarta.validation.constraints.NotNull;

public record CakeSearchByViewsRequest(
	Long offset,
	@NotNull
	Integer pageSize
) {
}
