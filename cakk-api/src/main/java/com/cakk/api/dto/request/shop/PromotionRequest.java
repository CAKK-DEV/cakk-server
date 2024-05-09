package com.cakk.api.dto.request.shop;

import jakarta.validation.constraints.NotNull;

public record PromotionRequest(

	@NotNull
	Long userId,

	@NotNull
	Long cakeShopId) {
}
