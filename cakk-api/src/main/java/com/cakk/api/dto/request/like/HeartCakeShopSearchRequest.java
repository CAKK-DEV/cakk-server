package com.cakk.api.dto.request.like;

import jakarta.validation.constraints.NotNull;

public record HeartCakeShopSearchRequest(
	Long cakeShopHeartId,
	@NotNull
	Integer pageSize
) {
}
