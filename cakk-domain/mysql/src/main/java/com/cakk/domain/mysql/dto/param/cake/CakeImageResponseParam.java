package com.cakk.domain.mysql.dto.param.cake;

public record CakeImageResponseParam(
	Long cakeShopId,
	Long cakeId,
	String cakeImageUrl
) {
}
