package com.cakk.api.dto.request.cake;

import jakarta.validation.constraints.NotNull;

public record CakeSearchByViewsRequest(
	@NotNull
	Long cursor,
	@NotNull
	Integer pageSize
) {
}
