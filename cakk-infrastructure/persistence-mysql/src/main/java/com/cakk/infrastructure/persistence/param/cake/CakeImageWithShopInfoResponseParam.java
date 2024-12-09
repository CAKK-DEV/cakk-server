package com.cakk.infrastructure.persistence.param.cake;

public record CakeImageWithShopInfoResponseParam(
	Long cakeShopId,
	Long cakeId,
	String cakeImageUrl,
	String thumbnailUrl,
	String shopName
) {
}
