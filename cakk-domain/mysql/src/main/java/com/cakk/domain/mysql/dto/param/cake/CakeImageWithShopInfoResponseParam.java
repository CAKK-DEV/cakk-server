package com.cakk.domain.mysql.dto.param.cake;

public record CakeImageWithShopInfoResponseParam(
	Long cakeShopId,
	Long cakeId,
	String cakeImageUrl,
	String thumbnailUrl,
	String shopName
) {
}
