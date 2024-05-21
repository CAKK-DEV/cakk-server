package com.cakk.domain.dto.param.cake;

public record CakeImageResponseParam(
	Long cakeShopId,
	Long cakeId,
	String cakeImageUrl
) {
}
