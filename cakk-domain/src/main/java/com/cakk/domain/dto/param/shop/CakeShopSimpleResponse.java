package com.cakk.domain.dto.param.shop;

public record CakeShopSimpleResponse(
	Long cakeShopId,
	String thumbnailUrl,
	String cakeShopName,
	String cakeShopBio
) {
}
