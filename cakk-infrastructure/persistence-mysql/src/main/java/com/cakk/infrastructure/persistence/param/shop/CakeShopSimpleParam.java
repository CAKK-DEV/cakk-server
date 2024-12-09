package com.cakk.infrastructure.persistence.param.shop;

public record CakeShopSimpleParam(
	Long cakeShopId,
	String thumbnailUrl,
	String cakeShopName,
	String cakeShopBio
) {
}
